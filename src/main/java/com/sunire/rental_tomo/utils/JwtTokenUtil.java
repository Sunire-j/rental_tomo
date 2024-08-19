package com.sunire.rental_tomo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {


    public static Map<String,String> createToken(String userid, String key, long expireTimeMs, long expireTimeMs_Refresh) {
        Claims claims = Jwts.claims();
        claims.put("userid", userid);

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

        return token;
    }

    public static boolean isExpired(String token, String secretKey) {
        return Jwts.parserBuilder().
                setSigningKey(secretKey).build().
                parseClaimsJws(token).getBody(). //Jws는 서명된 jwt, jwt는 서명되지 않은 jwt
                        getExpiration().before(new Date());
    }

    public static String getUserid(String token, String secretKey) {
        return Jwts.parserBuilder().
                setSigningKey(secretKey).build().
                parseClaimsJws(token).getBody().
                get("userid", String.class);
    }

}
