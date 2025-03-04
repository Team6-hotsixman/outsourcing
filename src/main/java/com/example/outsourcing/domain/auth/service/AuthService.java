package com.example.outsourcing.domain.auth.service;

import com.example.outsourcing.domain.auth.config.PasswordEncoder;
import com.example.outsourcing.domain.auth.dto.request.AuthLoginRequestDto;
import com.example.outsourcing.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.outsourcing.domain.auth.dto.response.AuthLoginResponseDto;
import com.example.outsourcing.domain.auth.dto.response.AuthSingupResponseDto;
import com.example.outsourcing.domain.auth.exception.AuthException;
import com.example.outsourcing.domain.auth.jwt.JwtUtil;
import com.example.outsourcing.domain.common.exception.InvalidRequestException;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthSingupResponseDto signup(AuthSignupRequestDto authSignupRequestDto) {

        if (userRepository.existsByEmail(authSignupRequestDto.getEmail())) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(authSignupRequestDto.getPassword());

        UserRole userRole = UserRole.of(authSignupRequestDto.getUserRole());

        User user = User.builder().
                email(authSignupRequestDto.getEmail()).
                password(encodedPassword).
                name(authSignupRequestDto.getName()).
                userRole(userRole).
                createdAt(LocalDateTime.now()).
                modifiedAt(LocalDateTime.now()).
                build();
        User savedUser = userRepository.save(user);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        return new AuthSingupResponseDto(bearerToken);
    }

    @Transactional(readOnly = true)
    public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
        User user = userRepository.findByEmail(authLoginRequestDto.getEmail()).orElseThrow(
                () -> new InvalidRequestException("가입되지 않은 유저입니다."));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(authLoginRequestDto.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new AuthLoginResponseDto(bearerToken);
    }
}