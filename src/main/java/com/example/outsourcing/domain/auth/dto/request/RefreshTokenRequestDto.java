package com.example.outsourcing.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshTokenRequestDto {
    private String refreshToken;

    public RefreshTokenRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}