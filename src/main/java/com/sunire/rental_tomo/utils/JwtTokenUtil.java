package com.sunire.rental_tomo.utils;

import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.repository.UserRepository;
import com.sunire.rental_tomo.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {


    public static Map<String,String> createToken(User user, String key, long expireTimeMs, long expireTimeMs_Refresh, JwtTokenService jwtTokenService) {
        Claims claims = Jwts.claims();
        claims.put("userid", user.getId());

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs_Refresh))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        Map<String,String> token = new HashMap<>();
        token.put("accessToken", accessToken);
        token.put("refreshToken", refreshToken);

        Date refreshtokenexpiration = JwtTokenUtil.getExpirationDate(refreshToken, key);
        Date refreshtokencreated = JwtTokenUtil.getCreatedDate(refreshToken, key);

        // 새로운 리프레시 토큰 DB에 저장
        jwtTokenService.saveRefreshToken(user, refreshToken, refreshtokenexpiration, refreshtokencreated);

        return token;
    }

    public static boolean isExpired(String token, String secretKey) {
        return Jwts.parserBuilder().
                setSigningKey(secretKey).build().
                parseClaimsJws(token).getBody(). //Jws는 서명된 jwt, jwt는 서명되지 않은 jwt
                        getExpiration().before(new Date());
    }

    public static Long getUserPk(String token, String secretKey) {
        return Jwts.parserBuilder().
                setSigningKey(secretKey).build().
                parseClaimsJws(token).getBody().
                get("userid", Long.class);
    }

    public static Date getExpirationDate(String token, String key) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration(); // 토큰의 만료일자를 반환
    }

    public static Date getCreatedDate(String token, String key) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        return claims.getIssuedAt(); // 토큰의 생성일자
    }

}
