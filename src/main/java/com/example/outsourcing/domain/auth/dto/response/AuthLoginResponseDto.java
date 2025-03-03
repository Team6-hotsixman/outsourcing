package com.example.outsourcing.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class AuthLoginResponseDto {

    private final String bearerToken;

    public AuthLoginResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
