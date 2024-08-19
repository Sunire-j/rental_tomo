package com.sunire.rental_tomo.service;

import com.sunire.rental_tomo.domain.dto.UserJoinRequest;
import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.exception.AppException;
import com.sunire.rental_tomo.exception.ErrorCode;
import com.sunire.rental_tomo.repository.UserRepository;
import com.sunire.rental_tomo.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.token.secret}")
    private String key;
    private final Long expireTimeMs = 1000*60* 60L;

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



        User user = User.builder()
                .userid(userJoinRequest.getUserid())
                .password(bCryptPasswordEncoder.encode(userJoinRequest.getPassword()))
                .nickname(userJoinRequest.getNickname())
                .phonenum(userJoinRequest.getPhonenum())
                .sex(userJoinRequest.getSex())
                .birth(userJoinRequest.getBirth())
                .email(userJoinRequest.getEmail())
                                        .build();

        userRepository.save(user);
        return "success";
    }

    public String login(UserJoinRequest userJoinRequest) {
        //username X
        User user = userRepository.findByUserid(userJoinRequest.getUserid())
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, userJoinRequest.getUserid() +"이 없습니다."));

        // wrong password
        if(!bCryptPasswordEncoder.matches(userJoinRequest.getPassword(), user.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비번 잘못됨");
        }

        //generate token and return

        System.out.println("key is " +key);
        return JwtTokenUtil.createToken(user.getUserid(), key,expireTimeMs);
    }
}
