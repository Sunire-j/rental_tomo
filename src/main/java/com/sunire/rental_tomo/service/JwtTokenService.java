package com.sunire.rental_tomo.service;

import com.sunire.rental_tomo.domain.entity.RefreshToken;
import com.sunire.rental_tomo.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(String userId, String token, Date expirationDate, Date createdDate) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expirationDate(expirationDate)
                .createdAt(createdDate)
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        log.info("refresh token {}", token);
        refreshTokenRepository.deleteByToken(token);
    }
}
