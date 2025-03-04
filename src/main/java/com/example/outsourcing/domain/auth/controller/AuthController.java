package com.example.outsourcing.domain.auth.controller;

import com.example.outsourcing.domain.auth.dto.request.AuthLoginRequestDto;
import com.example.outsourcing.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.outsourcing.domain.auth.dto.response.AuthLoginResponseDto;
import com.example.outsourcing.domain.auth.dto.response.AuthSingupResponseDto;
import com.example.outsourcing.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public AuthSingupResponseDto signup(@Valid @RequestBody AuthSignupRequestDto authSignupRequestDto) {
        return authService.signup(authSignupRequestDto);
    }

    @PostMapping("/login")
    public AuthLoginResponseDto login(@Valid @RequestBody AuthLoginRequestDto authLoginRequestDto) {
        return authService.login(authLoginRequestDto);
    }
}
