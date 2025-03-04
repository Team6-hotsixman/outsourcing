package com.example.outsourcing.domain.store.controller;

import com.example.outsourcing.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.*;
import com.example.outsourcing.domain.store.service.OwnerStoreService;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class OwnerStoreController {
    private final OwnerStoreService ownerStoreService;

    @PostMapping
    public ResponseEntity<SaveStoreResponseDto> saveStore(@RequestBody @Valid StoreSaveRequestDto requestDto) {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        return new ResponseEntity<>(ownerStoreService.saveStore(authUser,requestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<UpdateStoreResponseDto> updateStore(
            @PathVariable Long storeId,
            @RequestBody @Valid StoreUpdateRequestDto requestDto)
    {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        return new ResponseEntity<>(ownerStoreService.updateStore(storeId, authUser, requestDto), HttpStatus.OK);
    }

    @PatchMapping("/{storeId}/status")
    public ResponseEntity<StoreStatusResponseDto> updateStoreStatus(
            @PathVariable Long storeId,
            @RequestBody @Valid StoreStatusUpdateRequestDto requestDto
    ) {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        return new ResponseEntity<>(ownerStoreService.updateStoreStatus(authUser, storeId, requestDto), HttpStatus.OK);
    }

    @PatchMapping("/{storeId}/notice")
    public ResponseEntity<StoreNoticeResponseDto> updateStoreStatus(
            @PathVariable Long storeId,
            @RequestBody @Valid StoreNoticeResponseDto requestDto
    ) {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        return new ResponseEntity<>(ownerStoreService.updateStoreNotice(authUser, storeId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable Long storeId,
            @RequestBody @Valid StoreDeleteRequestDto requestDto
    ) {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        ownerStoreService.deleteStore(authUser, storeId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
