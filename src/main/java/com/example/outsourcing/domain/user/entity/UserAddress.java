package com.example.outsourcing.domain.user.entity;

import com.example.outsourcing.domain.common.entity.BaseEntity;
import com.example.outsourcing.domain.user.enums.AddressStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.events.Event;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAddress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name= "user_id")
    private User user;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private AddressStatus addressStatus;

    @Builder
    private UserAddress(User user, String address, AddressStatus addressStatus) {
        this.user = user;
        this.address = address;
        this.addressStatus = addressStatus;
    }

}
