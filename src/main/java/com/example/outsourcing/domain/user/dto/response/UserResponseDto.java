package com.example.outsourcing.domain.user.dto.response;

import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    private final String email;
    private final String name;


    public static UserResponseDto of(User user) {
        return new UserResponseDto(user.getEmail(),user.getName());
    }
}
