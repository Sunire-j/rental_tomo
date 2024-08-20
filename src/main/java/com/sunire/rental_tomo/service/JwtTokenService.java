package com.sunire.rental_tomo.service;

import com.sunire.rental_tomo.domain.entity.RefreshToken;
import com.sunire.rental_tomo.repository.RefreshTokenRepository;
import com.sunire.rental_tomo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
