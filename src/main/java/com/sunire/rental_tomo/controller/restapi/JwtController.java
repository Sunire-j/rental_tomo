package com.sunire.rental_tomo.controller.restapi;

import com.sunire.rental_tomo.domain.entity.RefreshToken;
import com.sunire.rental_tomo.enumFile.TokenName;
import com.sunire.rental_tomo.exception.AppException;
import com.sunire.rental_tomo.service.JwtTokenService;
import com.sunire.rental_tomo.utils.CookieUtil;
import com.sunire.rental_tomo.utils.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Map;

import static com.sunire.rental_tomo.exception.ErrorCode.INVALID_REFRESH_TOKEN;
import static com.sunire.rental_tomo.exception.ErrorCode.REFRESH_TOKEN_EXPIRED;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/token")
public class JwtController {

    private final JwtTokenService jwtTokenService;

    @Value("${jwt.token.access_expiration}")
    private Long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.token.refresh_expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;

    @Value("${jwt.token.secret}")
    private String secretkey;

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {

        String refreshToken = CookieUtil.getCookie(TokenName.REFRESH_TOKEN.getName(), request);
        System.out.println(refreshToken);
        // 리프레시 토큰 검증
        RefreshToken storedRefreshToken = jwtTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new AppException(INVALID_REFRESH_TOKEN, "잘못된 접근"));
        //로그인 재시도로 유도해야함, 프론트가 할 일
        if (storedRefreshToken.getExpirationDate().before(new Date())) {
            throw new AppException(REFRESH_TOKEN_EXPIRED, "로그인 만료");
            //이것도 로그인 재시도로 유도, 프론트가 할 일
        }

        String userId = JwtTokenUtil.getUserid(storedRefreshToken.getToken(), secretkey);
        Map<String, String> token = JwtTokenUtil.createToken(userId, secretkey, ACCESS_TOKEN_EXPIRATION, REFRESH_TOKEN_EXPIRATION, jwtTokenService);

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

        return ResponseEntity.ok().body("토큰 재발급 성공");
    }

}
