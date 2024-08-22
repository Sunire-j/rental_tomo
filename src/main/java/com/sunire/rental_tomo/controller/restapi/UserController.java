package com.sunire.rental_tomo.controller.restapi;

import com.sunire.rental_tomo.domain.dto.UserJoinRequest;
import com.sunire.rental_tomo.enumFile.TokenName;
import com.sunire.rental_tomo.service.JwtTokenService;
import com.sunire.rental_tomo.service.UserService;
import com.sunire.rental_tomo.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest userJoinRequest) {

        log.info("UserJoinRequest: {}", userJoinRequest);
        String hi = userService.join(userJoinRequest);
        return new ResponseEntity<>("User joined successfully", HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserJoinRequest userJoinRequest) {
        Map<String, String> token = userService.login(userJoinRequest);

        Cookie accessTokenCookie = CookieUtil.createCookie("accessToken", token.get("accessToken"), 604800);
        Cookie refreshTokenCookie = CookieUtil.createCookie("refreshToken", token.get("refreshToken"), 604800);

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
        }

        return ResponseEntity.ok().body("성공");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = CookieUtil.getCookie_Bearer(TokenName.ACCESS_TOKEN.getName(), request);
        String id = userService.getId(token);
        jwtTokenService.deleteRefreshToken(token);

        Cookie accessTokenCookie = CookieUtil.createCookie("accessToken", null, 0);
        Cookie refreshTokenCookie = CookieUtil.createCookie("refreshToken", null, 0);

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
        }

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("/login"))
                .build();
        //프론트에서 엑세스 토큰 삭제시켜야함
        //ㄴㄴ 여기에서 깡통쿠키 만들어서 보내버려야함
    }


    @GetMapping("/nickname")
    public ResponseEntity<String> nickname(HttpServletRequest request) {
        String token = CookieUtil.getCookie_Bearer(TokenName.ACCESS_TOKEN.getName(), request);
        System.out.println("컨트롤러 닉네임 토큰 : " + token);
        String name = userService.nickname(token);
        return ResponseEntity.ok().body(name);
    }

    @GetMapping("getId")
    public ResponseEntity<String> getId(HttpServletRequest request) {
        String token = CookieUtil.getCookie_Bearer(TokenName.ACCESS_TOKEN.getName(), request);
        String id = userService.getId(token);
        return ResponseEntity.ok().body(id);
    }
}


