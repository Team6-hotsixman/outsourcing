package com.example.outsourcing.domain.auth.service;

import com.example.outsourcing.domain.auth.config.PasswordEncoder;
import com.example.outsourcing.domain.auth.dto.request.AuthLoginRequestDto;
import com.example.outsourcing.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.outsourcing.domain.auth.dto.request.RefreshTokenRequestDto;
import com.example.outsourcing.domain.auth.dto.response.AuthLoginResponseDto;
import com.example.outsourcing.domain.auth.dto.response.AuthSingupResponseDto;
import com.example.outsourcing.domain.auth.entity.AuthRefreshToken;
import com.example.outsourcing.domain.auth.repository.RefreshTokenRepository;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.jwt.JwtUtil;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.enums.UserStatus;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AuthSingupResponseDto signup(AuthSignupRequestDto authSignupRequestDto) {
        String encodedPassword = passwordEncoder.encode(authSignupRequestDto.getPassword());
        UserRole userRole = UserRole.of(authSignupRequestDto.getUserRole());

        Optional<User> existingUser = userRepository.findByEmail(authSignupRequestDto.getEmail());

        if (existingUser.isPresent()) {
            if (existingUser.get().getUserStatus() == UserStatus.DELETE) {
                throw new ApplicationException(ErrorCode.USER_STATUS_DELETE);
            }
            throw new ApplicationException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .email(authSignupRequestDto.getEmail())
                .password(encodedPassword)
                .name(authSignupRequestDto.getName())
                .point(0)
                .userRole(userRole)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        String accessToken = jwtUtil.createAccessToken(savedUser.getId(), savedUser.getEmail(), userRole);
        String refreshToken = jwtUtil.createRefreshToken(savedUser.getId());

        // Refresh Token 저장
        AuthRefreshToken tokenEntity = AuthRefreshToken.builder()
                .userId(savedUser.getId())
                .refreshToken(refreshToken)
                .build();
        refreshTokenRepository.save(tokenEntity);

        return new AuthSingupResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
        User user = userRepository.findByEmail(authLoginRequestDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER));

        if (user.getUserStatus() == UserStatus.DELETE) {
            throw new ApplicationException(ErrorCode.USER_STATUS_DELETE);
        }

        if (!passwordEncoder.matches(authLoginRequestDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.PASSWORD_ARGUMENT_NOT_VALID);
        }

        String refreshToken = jwtUtil.createRefreshToken(user.getId());
        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getUserRole());

        refreshTokenRepository.findByUserId(user.getId()).ifPresent(refreshTokenRepository::delete);
        refreshTokenRepository.save(new AuthRefreshToken(user.getId(), refreshToken));

        return new AuthLoginResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public void logout(AuthUser authUser) {
        refreshTokenRepository.deleteByUserId(authUser.getId());
    }

    @Transactional
    public AuthLoginResponseDto refreshToken(RefreshTokenRequestDto request) {
        log.info("Received refresh token: {}", request.getRefreshToken());

        String refreshToken = jwtUtil.substringToken(request.getRefreshToken());

        if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            throw new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        AuthRefreshToken storedToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN));

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER));

        String newAccessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getUserRole());
        String newRefreshToken = jwtUtil.createRefreshToken(user.getId());

        storedToken.updateToken(newRefreshToken); // 기존 Refresh Token 업데이트
        refreshTokenRepository.save(storedToken); // DB 저장

        log.info("Refreshed tokens for user {}", user.getEmail());
        return new AuthLoginResponseDto(newAccessToken, newRefreshToken);
    }
}
