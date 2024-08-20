package com.sunire.rental_tomo.controller.restapi;

import com.sunire.rental_tomo.domain.entity.RefreshToken;
import com.sunire.rental_tomo.exception.AppException;
import com.sunire.rental_tomo.service.JwtTokenService;
import com.sunire.rental_tomo.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("refresh_token") String refreshToken) {

        // 리프레시 토큰 검증
        RefreshToken storedRefreshToken = jwtTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new AppException(INVALID_REFRESH_TOKEN, "잘못된 접근"));
        //로그인 재시도로 유도해야함
        if (storedRefreshToken.getExpirationDate().before(new Date())) {
            throw new AppException(REFRESH_TOKEN_EXPIRED, "로그인 만료");
            //이것도 로그인 재시도로 유도
        }

        // 새로운 액세스 토큰 및  토큰 생성
        String userId = JwtTokenUtil.getUserid(storedRefreshToken.getToken(), secretkey);
        Map<String, String> tokens = JwtTokenUtil.createToken(userId, secretkey, ACCESS_TOKEN_EXPIRATION, REFRESH_TOKEN_EXPIRATION, jwtTokenService);

        return ResponseEntity.ok(tokens);
    }

}
