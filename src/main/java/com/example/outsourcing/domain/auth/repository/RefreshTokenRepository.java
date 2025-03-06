package com.example.outsourcing.domain.auth.repository;

import com.example.outsourcing.domain.auth.entity.AuthRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<AuthRefreshToken, Long> {
    Optional<AuthRefreshToken> findByRefreshToken(String refreshToken);
    Optional<AuthRefreshToken> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}