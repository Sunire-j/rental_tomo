package com.sunire.rental_tomo.config;

import com.sunire.rental_tomo.enumFile.TokenName;
import com.sunire.rental_tomo.service.UserService;
import com.sunire.rental_tomo.utils.CookieUtil;
import com.sunire.rental_tomo.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Value("${jwt.token.secret}")
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token = CookieUtil.getCookie(TokenName.ACCESS_TOKEN.getName(), request);
        final String refresh = CookieUtil.getCookie(TokenName.REFRESH_TOKEN.getName(), request);

        String requestURI = request.getRequestURI();
        if(requestURI.equals("/api/v1/token/refresh")||requestURI.equals("/api/v1/users/logout")) {
            filterChain.doFilter(request,response);
            return;
        }

        //1. 토큰이 둘 다 없으면 그냥 jwt없는걸로 취급
        if (token == null && refresh == null) {
            filterChain.doFilter(request, response);
            return;
        }

        //2. refresh가 만료인지 먼저 체크, refresh가 만료면 access도 당연히 만료이므로
        try {
            if (!JwtTokenUtil.isExpired(refresh, secretKey)) {//refresh null체크는 1에서 했음

                //3. access도 만료인지 체크, 만료라면 refresh로 리디렉션
                try {
                    if (!JwtTokenUtil.isExpired(token, secretKey)) {//token null체크 1에서 함
                        //아무것도 없이 그냥 진행
                        System.out.println("cool!");
                    }

                } catch (ExpiredJwtException e) {
                    response.sendRedirect("/api/v1/token/refresh");
                    //이게 좀 문제임. 결국 리프레시로 가더라도 바로 컨트롤러로 들어가지 않고 필터체인을 거치고있음.
                    return;
                }

            }
        } catch (ExpiredJwtException e) {
            response.sendRedirect("/api/v1/users/logout");//리프레시 만료라면 쿠키 다 죽여버리게 /logout으로 이동
            return;
        }
        //expired 확인

        //getUserid
        String username = JwtTokenUtil.getUserid(token, secretKey);
        log.info("username: {}", username);

        //add auth
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        //detail
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}
