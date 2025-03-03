package com.example.outsourcing.domain.user.dto.response;

import com.example.outsourcing.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private final Long id;
    private final String email;

    public static UserResponse of(User user) {
        return new UserResponse(user.getId(),user.getEmail());
    }
}
