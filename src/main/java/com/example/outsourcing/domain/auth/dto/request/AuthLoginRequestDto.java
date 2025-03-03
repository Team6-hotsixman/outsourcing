package com.example.outsourcing.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthLoginRequestDto {

    @NotBlank @Email
    private final String email;

    @NotBlank
    private final String password;

    @Builder
    public AuthLoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
