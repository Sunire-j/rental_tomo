package com.sunire.rental_tomo.service;

import com.sunire.rental_tomo.domain.dto.UserInfoEditRequest;
import com.sunire.rental_tomo.domain.dto.UserJoinRequest;
import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.enumFile.TokenName;
import com.sunire.rental_tomo.exception.AppException;
import com.sunire.rental_tomo.exception.ErrorCode;
import com.sunire.rental_tomo.repository.UserRepository;
import com.sunire.rental_tomo.utils.CookieUtil;
import com.sunire.rental_tomo.utils.Gravatar_HashUtil;
import com.sunire.rental_tomo.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenService jwtTokenService;

    @Value("${jwt.token.secret}")
    private String key;
    private final Long expireTimeMs = 1000*60* 60L;//60분
    private final Long expireTimeMs_Refresh = 1000*60* 60 * 24 * 7L;//7일

    public String join(UserJoinRequest userJoinRequest) {

        //아이디 중복 검사
        userRepository.findByUserid(userJoinRequest.getUserid())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userJoinRequest.getUserid()+"는 이미 있습니다.");
                });

        //비밀번호는 프론트에서 검사

        //폰번호 유효성 프론트에서 검사

        //닉네임 중복 검사
        userRepository.findByNickname(userJoinRequest.getNickname())
                .ifPresent(user->{
                    throw new AppException(ErrorCode.NICKNAME_DUPLICATED, userJoinRequest.getNickname()+"는 이미 있습니다.");
                });

        //birth는 달력으로 입력받기

        String hash = Gravatar_HashUtil.sha256Hex(userJoinRequest.getEmail().toLowerCase());
        String url = "https://gravatar.com/avatar/"+hash+"?s=200&d=retro&r=pg";



        User user = User.builder()
                .userid(userJoinRequest.getUserid())
                .password(bCryptPasswordEncoder.encode(userJoinRequest.getPassword()))
                .nickname(userJoinRequest.getNickname())
                .phonenum(userJoinRequest.getPhonenum())
                .sex(userJoinRequest.getSex())
                .birth(userJoinRequest.getBirth())
                .email(userJoinRequest.getEmail())
                .gravatarUrl(url)
                                        .build();

        userRepository.save(user);
        return "success";
    }

    public Map<String, String> login(UserJoinRequest userJoinRequest) {
        //username X
        User user = userRepository.findByUserid(userJoinRequest.getUserid())
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, userJoinRequest.getUserid() +"이 없습니다."));

        // wrong password
        if(!bCryptPasswordEncoder.matches(userJoinRequest.getPassword(), user.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비번 잘못됨");
        }

        //generate token and return



        return JwtTokenUtil.createToken(user, key,expireTimeMs, expireTimeMs_Refresh, jwtTokenService);
    }

    public String nickname(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.TOKEN_NOT_FOUND, "토큰이 유효하지 않거나, 존재하지 않습니다.");
        }

        String token_temp = token.substring(7);
        Long userpk = JwtTokenUtil.getUserPk(token_temp, key);


        return userRepository.findById(userpk)
                .map(user -> user.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    public String getId(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.TOKEN_NOT_FOUND, "토큰이 유효하지 않거나, 존재하지 않습니다.");
        }

        String token_temp = token.substring(7);
//        로그인하는 아이디를 얻는거겠지
        Long userpk = JwtTokenUtil.getUserPk(token_temp, key);
        User user = userRepository.findById(userpk).orElse(null);
        return user.getUserid();
    }

    public String isLogin(HttpServletRequest request){
        String token = CookieUtil.getCookie_Bearer(TokenName.ACCESS_TOKEN.getName(), request);
        //로그인아니면 null, 맞으면 id반환
        if(token==null || !token.startsWith("Bearer ")){
            return null;
        }
        String token_temp = token.substring(7);
        Long userpk = JwtTokenUtil.getUserPk(token_temp, key);
        Optional<User> user = userRepository.findById(userpk);
        String username = user.map(User::getNickname).orElse(null);

        return username;
    }

    public Long getPk(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.TOKEN_NOT_FOUND, "토큰이 유효하지 않거나, 존재하지 않습니다.");
        }

        String token_temp = token.substring(7);
        //토큰꺼냄 로그인아이디니까 pk키를 구해야함
        Long userpk = JwtTokenUtil.getUserPk(token_temp, key);

        return userpk;
    }

    public Optional<User> userInfo(String name){
        return userRepository.findByNickname(name);
    }

    public Optional<User> userInfoWithUserId(String userid){
        return userRepository.findByUserid(userid);
    }

    public void updateUser(UserInfoEditRequest userInfoEditRequest){
        User user = userRepository.findById(userInfoEditRequest.getId()).orElse(null);

        if(!Objects.equals(user.getNickname(), userInfoEditRequest.getNickname())){
            userRepository.findByNickname(userInfoEditRequest.getNickname())
                    .ifPresent(user_->{
                        throw new AppException(ErrorCode.NICKNAME_DUPLICATED, userInfoEditRequest.getNickname()+"는 이미 있습니다.");
                    });
        }

        String hash = Gravatar_HashUtil.sha256Hex(userInfoEditRequest.getEmail().toLowerCase());
        String url = "https://gravatar.com/avatar/"+hash+"?s=200&d=retro&r=pg";


        user.setNickname(userInfoEditRequest.getNickname());
        user.setEmail(userInfoEditRequest.getEmail());
        user.setSns(userInfoEditRequest.getSns());
        user.setIntroduce(userInfoEditRequest.getIntroduce());
        user.setGravatarUrl(url);

        userRepository.save(user);
    }

    public void updatePwd(String password, Long id){
        User user = userRepository.findById(id).orElse(null);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    public void deleteId(Long id) {
        User user = userRepository.findById(id).orElse(null);
        user.setPassword(bCryptPasswordEncoder.encode(key));
        user.setSns(null);
        user.setNickname("탈퇴한 회원");
        user.setUserid(String.valueOf(user.getId()));
        user.setEmail("NOT EXIST");
        user.setPhonenum("NOT EXIST");

        userRepository.save(user);
    }
}
