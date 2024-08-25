package com.sunire.rental_tomo.controller.restapi;

import com.sunire.rental_tomo.domain.entity.RefreshToken;
import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.enumFile.TokenName;
import com.sunire.rental_tomo.repository.UserRepository;
import com.sunire.rental_tomo.service.JwtTokenService;
import com.sunire.rental_tomo.service.UserService;
import com.sunire.rental_tomo.utils.CookieUtil;
import com.sunire.rental_tomo.utils.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/token")
public class JwtController {

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${jwt.token.access_expiration}")
    private Long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.token.refresh_expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;

    @Value("${jwt.token.secret}")
    private String secretkey;

    @GetMapping("/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {

        String refreshToken = CookieUtil.getCookie(TokenName.REFRESH_TOKEN.getName(), request);

        // 리프레시 토큰 검증
        RefreshToken storedRefreshToken = jwtTokenService.findByToken(refreshToken)
                .orElse(null);
        if(storedRefreshToken == null) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create("/api/v1/users/logout"))
                    .build();
        }
        //로그인 재시도로 유도해야함, 프론트가 할 일
        if (storedRefreshToken.getExpirationDate().before(new Date())) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create("/api/v1/users/logout"))
                    .build();
        }

        Long userpk = JwtTokenUtil.getUserPk(refreshToken, secretkey);
        User user = userRepository.findById(userpk).orElse(null);

        Map<String, String> token = JwtTokenUtil.createToken(user, secretkey, ACCESS_TOKEN_EXPIRATION, REFRESH_TOKEN_EXPIRATION, jwtTokenService);

        Cookie accessTokenCookie = new Cookie("accessToken", token.get("accessToken"));
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60 * 24*7);
        accessTokenCookie.setSecure(true);

        Cookie refreshTokenCookie = new Cookie("refreshToken", token.get("refreshToken"));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24*7); // 쿠키 만료 시간 설정 (7일)
        refreshTokenCookie.setSecure(true);

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
        }

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("/"))
                .build();
    }

}
