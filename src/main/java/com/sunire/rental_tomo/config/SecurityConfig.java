package com.sunire.rental_tomo.config;

import com.sunire.rental_tomo.repository.UserRepository;
import com.sunire.rental_tomo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final UserRepository userRepository;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserRepository userRepository) throws Exception {

        log.info("secretkey : {}", secretKey);

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize->
                        authorize.requestMatchers(
                                "/api/v1/users/join",
                                        "/api/v1/users/login",
                                        "/",
                                        "/js/**", "/css/**", "/img/**",
                                        "/favicon.ico","/index"
                                ,"/login","/join/**",
                                        "/image/**","/api/v1/token/refresh", "api/v1/users/logout"
                                ,"/info/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/reviews/write", "/api/v1/users/edit", "/api/v1/seller/**", "/api/v1/users/follow/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/nickname", "/api/v1/users/getId",
                                        "/mypage/**", "/seller/**").authenticated()
                )
                .sessionManagement(config->config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(userRepository, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}