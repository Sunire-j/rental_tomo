package com.sunire.rental_tomo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {


    public static String createToken(String userid, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userid", userid);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
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
