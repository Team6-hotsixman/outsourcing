package com.example.outsourcing.domain.user.entity;

import com.example.outsourcing.domain.common.entity.BaseEntity;
import com.example.outsourcing.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    @Builder
    public User(String email,String password,UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    // 유저 point 차감 메소드
    public void subtractPoint(Integer pointToSubtract) {
        if (this.point < pointToSubtract) {
            throw new RuntimeException("포인트가 부족합니다.");
        }
        this.point -= pointToSubtract;
    }
}
