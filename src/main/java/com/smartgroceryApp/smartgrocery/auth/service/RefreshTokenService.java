package com.smartgroceryApp.smartgrocery.auth.service;

import com.smartgroceryApp.smartgrocery.common.exception.UnauthorizedException;
import com.smartgroceryApp.smartgrocery.security.jwt.JwtProperties;
import com.smartgroceryApp.smartgrocery.user.entity.RefreshToken;
import com.smartgroceryApp.smartgrocery.user.entity.User;
import com.smartgroceryApp.smartgrocery.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Transactional
    public RefreshToken createRefreshToken(User user, String token) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiresAt(
                LocalDateTime.ofInstant(
                        Instant.now().plusMillis(jwtProperties.getRefreshTokenExpiration()),
                        ZoneId.systemDefault()
                )
        );
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getValidToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("invalid token!"));

        if (refreshToken.isRevoked()) {
            throw new UnauthorizedException("refresh token revoked!");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("refresh token expired!");
        }

        return refreshToken;
    }

    public void revokeToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
