package com.example.outsourcing.domain.auth.controller;

import com.example.outsourcing.domain.auth.dto.request.AuthLoginRequestDto;
import com.example.outsourcing.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.outsourcing.domain.auth.dto.request.RefreshTokenRequestDto;
import com.example.outsourcing.domain.auth.dto.response.AuthLoginResponseDto;
import com.example.outsourcing.domain.auth.dto.response.AuthSingupResponseDto;
import com.example.outsourcing.domain.auth.service.AuthService;
import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.exception.InvalidRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public AuthSingupResponseDto signup(@Valid @RequestBody AuthSignupRequestDto authSignupRequestDto) {
        return authService.signup(authSignupRequestDto);
    }

    @PostMapping("/auth/login")
    public AuthLoginResponseDto login(@Valid @RequestBody AuthLoginRequestDto authLoginRequestDto) {
        return authService.login(authLoginRequestDto);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<AuthLoginResponseDto> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        AuthLoginResponseDto response = authService.refreshToken(new RefreshTokenRequestDto(refreshToken));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<String> logout(@Auth AuthUser authUser) {
        authService.logout(authUser);
        return ResponseEntity.ok("로그아웃되었습니다.");
    }
}
