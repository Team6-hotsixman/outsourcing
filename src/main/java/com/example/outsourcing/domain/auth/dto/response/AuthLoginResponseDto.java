package com.example.outsourcing.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthLoginResponseDto {

    private final String accessToken;
    private final String refreshToken;

    @Builder
    public AuthLoginResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
