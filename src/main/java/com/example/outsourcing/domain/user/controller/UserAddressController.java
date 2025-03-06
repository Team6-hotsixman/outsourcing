package com.example.outsourcing.domain.user.controller;

import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.user.dto.request.UserAddressCreateDto;
import com.example.outsourcing.domain.user.dto.request.UserAddressUpdateRequestDto;
import com.example.outsourcing.domain.user.dto.response.UserAddressResponse;
import com.example.outsourcing.domain.user.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-addresses")
public class UserAddressController {
    private final UserAddressService userAddressService;

    @GetMapping
    public ResponseEntity<List<UserAddressResponse>> getUserAddresses(@Auth AuthUser authUser) {
        List<UserAddressResponse> userAddresses = userAddressService.getUserAddresses(authUser);
        return ResponseEntity.ok(userAddresses);
    }

    @PostMapping
    public ResponseEntity<UserAddressResponse> saveUserAddress(@Auth AuthUser authUser,
                                                               @RequestBody UserAddressCreateDto userAddressCreateDto) {

        UserAddressResponse userAddressResponse = userAddressService.saveUserAddress(authUser, userAddressCreateDto);

        return ResponseEntity.ok(userAddressResponse);
    }

    @PatchMapping("/{userAddressId}")
    public ResponseEntity<UserAddressResponse> updateUserAddress(@Auth AuthUser authUser,
                                                                 @PathVariable Long userAddressId,
                                                                 @RequestBody UserAddressUpdateRequestDto userAddressUpdateRequestDto) {
        UserAddressResponse userAddressResponse = userAddressService.updateUserAddress(authUser, userAddressId, userAddressUpdateRequestDto);

        return ResponseEntity.ok(userAddressResponse);
    }

    @PatchMapping("/{userAddressId}/default")
    public ResponseEntity<UserAddressResponse> updateUserAddressToDefault(@Auth AuthUser authUser,
                                                                          @PathVariable Long userAddressId) {
        UserAddressResponse userAddressResponse = userAddressService.updateUserAddressToDefault(authUser, userAddressId);

        return ResponseEntity.ok(userAddressResponse);
    }

    @DeleteMapping("/{userAddressId}")
    public ResponseEntity<Void> deleteUserAddress(@Auth AuthUser authUser,
                                                                 @PathVariable Long userAddressId) {
        userAddressService.deleteUserAddress(authUser, userAddressId);

        return ResponseEntity.ok().build();
    }
}
