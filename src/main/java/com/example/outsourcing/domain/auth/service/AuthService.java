package com.example.outsourcing.domain.auth.service;

import com.example.outsourcing.domain.auth.config.PasswordEncoder;
import com.example.outsourcing.domain.auth.dto.request.AuthLoginRequestDto;
import com.example.outsourcing.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.outsourcing.domain.auth.dto.response.AuthLoginResponseDto;
import com.example.outsourcing.domain.auth.dto.response.AuthSingupResponseDto;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.jwt.JwtUtil;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.enums.UserStatus;
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
            throw new ApplicationException(ErrorCode.DUPLICATE_EMAIL);
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
        UserStatus userStatus = UserStatus.ACTIVE;

        User savedUser = userRepository.save(user);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        return new AuthSingupResponseDto(bearerToken);
    }

    @Transactional(readOnly = true)
    public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
        User user = userRepository.findByEmail(authLoginRequestDto.getEmail()).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(authLoginRequestDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.PASSWORD_ARGUMENT_NOT_VALID);
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new AuthLoginResponseDto(bearerToken);
    }
}