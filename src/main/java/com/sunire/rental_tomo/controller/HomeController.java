package com.sunire.rental_tomo.controller;

import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.enumFile.TokenName;
import com.sunire.rental_tomo.service.JwtTokenService;
import com.sunire.rental_tomo.service.UserService;
import com.sunire.rental_tomo.utils.CookieUtil;
import com.sunire.rental_tomo.utils.setLoginStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {

        String name = userService.isLogin(request);
        if(name!=null){
            model = setLoginStatus.setLogin(model, name);
        }
        return "index.th.html";
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if(name!=null){
            model = setLoginStatus.setLogin(model, name);

        }
        return "realindex.th.html";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if(name!=null){
            model = setLoginStatus.setLogin(model, name);

        }
        return "user/login.th.html";
    }

    @GetMapping("/join/1")
    public String join(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if(name!=null){
            model = setLoginStatus.setLogin(model, name);

        }
        return "user/join_step1.th.html";
    }

    @GetMapping("/join/2")
    public String join2(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if(name!=null){
            model = setLoginStatus.setLogin(model, name);

        }
        return "user/join_step2.th.html";
    }

//    마이페이지 시작
    @GetMapping("/mypage")
    public String mypage(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if(name!=null){
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

//        들어가야 하는 정보 아이디, 휴대폰번호, 성별, SNS, 닉네임, 이메일, 생일
        //자기소개 테이블 추가해야하고, 프로필사진 기능 추가하고 불러와야함
        return "user/mypage_intro.th.html";
    }

    @GetMapping("/mypage/edit")
    public String mypage_edit(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if(name!=null){
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

//        들어가야 하는 정보 아이디, 휴대폰번호, 성별, SNS, 닉네임, 이메일, 생일
        //자기소개 테이블 추가해야하고, 프로필사진 기능 추가하고 불러와야함
        return "user/mypage_editinfo.th.html";
    }

    @GetMapping("/mypage/deleteid")
    public String deleteid(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if(name!=null){
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

        return "user/mypage_deleteid1.th.html";
    }

    @GetMapping("/mypage/deleteid2")
    public ResponseEntity<String> deleteid2(HttpServletRequest request) {
        String token = CookieUtil.getCookie_Bearer(TokenName.ACCESS_TOKEN.getName(), request);
        String refresh = CookieUtil.getCookie(TokenName.REFRESH_TOKEN.getName(), request);
        Long id = userService.getPk(token);
        //foreign키때문에 user테이블부터 날리면 이슈임.
        //결국 user테이블에도 userid가 있음.
        //어차피 토큰테이블만 특별하게 userid를 받아온거니, 토큰테이블에서 아예 지워버린 뒤 userid를 바꿔야함
        jwtTokenService.deleteRefreshToken(refresh);

        userService.deleteId(id);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("/"))
                .build();
    }
}
