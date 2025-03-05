package com.example.outsourcing.domain.user.entity;

import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.entity.BaseEntity;
import com.example.outsourcing.domain.user.dto.response.UserResponseDto;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private Integer point;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Builder
    private User(
            String email,
            String password,
            String name,
            Integer point,
            UserRole userRole,
            UserStatus userStatus,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.point = point;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public void getUser(UserResponseDto userResponseDto) {
        this.email = userResponseDto.getEmail();
        this.name = userResponseDto.getName();
    }

    private User(Long id, String email, UserRole userRole){
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    public static User fromAuthUser(AuthUser authUser) {
        return new User(authUser.getId(), authUser.getEmail(), authUser.getUserRole());
    }

    public void passwordUpdate(String password) {
        this.password = password;
    }

    public void UpdateUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    // 유저 point 차감 메소드
    public void subtractPoint(Integer pointToSubtract) {
        if (this.point < pointToSubtract) {
            throw new RuntimeException("포인트가 부족합니다.");
        }
        this.point -= pointToSubtract;
    }
}
