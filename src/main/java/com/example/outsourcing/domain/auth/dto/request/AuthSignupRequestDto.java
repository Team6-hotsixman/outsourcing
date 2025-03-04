package com.example.outsourcing.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthSignupRequestDto {

    @NotBlank @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "새 비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = ".*\\d.*", message = "새 비밀번호는 숫자를 포함해야 합니다.")
    @Pattern(regexp = ".*[A-Z].*", message = "새 비밀번호는 대문자를 포함해야 합니다.")
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
