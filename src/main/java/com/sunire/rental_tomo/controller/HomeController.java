package com.sunire.rental_tomo.controller;

import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.enumFile.TokenName;
import com.sunire.rental_tomo.service.UserService;
import com.sunire.rental_tomo.utils.CookieUtil;
import com.sunire.rental_tomo.utils.setLoginStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

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
}
