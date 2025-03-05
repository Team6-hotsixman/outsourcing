package com.example.outsourcing.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class AuthSingupResponseDto {

    private final String accessToken;
    private final String refreshToken;

    public AuthSingupResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
