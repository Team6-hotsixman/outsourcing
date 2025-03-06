package com.example.outsourcing.domain.user.dto.response;

import com.example.outsourcing.domain.user.entity.UserAddress;
import com.example.outsourcing.domain.user.enums.AddressStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAddressResponse {
    private final Long id;
    private final String address;
    private final boolean isDefault;

    public static UserAddressResponse of(UserAddress userAddress) {
        return new UserAddressResponse(userAddress.getId(), userAddress.getAddress(), userAddress.getAddressStatus() == AddressStatus.DEFAULT);
    }
}
