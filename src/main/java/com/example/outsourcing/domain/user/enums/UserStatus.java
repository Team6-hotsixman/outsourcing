package com.example.outsourcing.domain.user.enums;

public enum UserStatus {
    DELETE("DELETE"),ACTIVE("ACTIVE");

    private String status;

    UserStatus(String status) {
        this.status =status;
    }
}
