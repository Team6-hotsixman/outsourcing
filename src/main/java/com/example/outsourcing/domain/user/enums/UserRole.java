package com.example.outsourcing.domain.user.enums;

import com.example.outsourcing.domain.common.exception.InvalidRequestException;

import java.util.Arrays;

public enum UserRole {
    ADMIN,USER,OWNER;

    public static UserRole of(String role) {
        if (role == null) {
            throw new InvalidRequestException("role 값이 null 입니다.");
        }

        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 UserRole: " + role));
    }
}
