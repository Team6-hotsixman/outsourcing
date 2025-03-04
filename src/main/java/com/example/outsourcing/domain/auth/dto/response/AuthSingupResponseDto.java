package com.example.outsourcing.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class AuthSingupResponseDto {

    private final String bearerToken;

    public AuthSingupResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
