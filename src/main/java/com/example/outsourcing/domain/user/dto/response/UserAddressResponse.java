package com.example.outsourcing.domain.user.dto.response;

import com.example.outsourcing.domain.user.entity.UserAddress;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAddressResponse {
    Long id;
    String address;

    public static UserAddressResponse of(UserAddress userAddress) {
        return new UserAddressResponse(userAddress.getId(), userAddress.getAddress());
    }
}
