package com.example.outsourcing.domain.user.entity;

import com.example.outsourcing.domain.common.entity.BaseEntity;
import com.example.outsourcing.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
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


    @Builder
    private User(
            String email,
            String password,
            //String name,
            //Integer point,
            UserRole userRole
            //LocalDateTime createdAt,
            //LocalDateTime modifiedAt

    ) {
        this.email = email;
        this.password = password;
        //this.name = name;
        //this.point = point;
        this.userRole = userRole;
        //this.createdAt = createdAt;
        //this.modifiedAt = modifiedAt;

    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
