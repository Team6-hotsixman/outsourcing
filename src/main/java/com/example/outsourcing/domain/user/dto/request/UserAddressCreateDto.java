package com.example.outsourcing.domain.user.dto.request;

import com.example.outsourcing.domain.user.enums.AddressStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAddressCreateDto {
    String address;
}
