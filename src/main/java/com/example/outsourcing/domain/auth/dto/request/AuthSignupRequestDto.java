package com.example.outsourcing.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthSignupRequestDto {

    @NotBlank @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String userRole;

    private String name;

    @Builder
    private AuthSignupRequestDto(String email,String password,String name, String userRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this. userRole = userRole;
    }
}
