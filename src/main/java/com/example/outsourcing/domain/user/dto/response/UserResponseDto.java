package com.example.outsourcing.domain.user.dto.response;

import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String name;
    private final String email;
    private final UserRole userRole;


    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.userRole = user.getUserRole();
    }
}
