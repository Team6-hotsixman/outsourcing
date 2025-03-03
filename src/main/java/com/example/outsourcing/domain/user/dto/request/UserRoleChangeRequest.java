package com.example.outsourcing.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserRoleChangeRequest {

    private final String role;

    public UserRoleChangeRequest(String role) {
        this.role = role;
    }
}
