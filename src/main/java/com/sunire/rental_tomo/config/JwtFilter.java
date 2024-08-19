package com.sunire.rental_tomo.config;

import com.sunire.rental_tomo.service.UserService;
import com.sunire.rental_tomo.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authHeader: {}", authHeader);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request,response);
            return;
        }

        //token 꺼내기
        final String token = authHeader.substring(7);
        log.info("token: {}", token);

        //expired 확인

        if(JwtTokenUtil.isExpired(token,secretKey)){
            log.error("expired token");
            filterChain.doFilter(request,response);
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        log.info("secretKey in jwtfilter: {}", secretKey);

        //getUserid
        String username = JwtTokenUtil.getUserid(token,secretKey);
        log.info("username: {}", username);

        //add auth
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        //detail
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request,response);

    }
}
