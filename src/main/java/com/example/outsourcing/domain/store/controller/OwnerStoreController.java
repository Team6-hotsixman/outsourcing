package com.example.outsourcing.domain.store.controller;

import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.annotation.Owner;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.store.dto.request.*;
import com.example.outsourcing.domain.store.dto.response.StoreSaveResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreNoticeResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreStatusResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreUpdateResponseDto;
import com.example.outsourcing.domain.store.service.OwnerStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
@Owner
public class OwnerStoreController {

    private final OwnerStoreService ownerStoreService;

    @PostMapping
    public ResponseEntity<StoreSaveResponseDto> saveStore(
            @Auth AuthUser authUser,
            @RequestBody @Valid StoreSaveRequestDto requestDto
    ) {
        return ResponseEntity.ok(ownerStoreService.saveStore(authUser, requestDto));
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<StoreUpdateResponseDto> updateStore(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody @Valid StoreUpdateRequestDto requestDto)
    {
        return ResponseEntity.ok(ownerStoreService.updateStore(storeId, authUser, requestDto));
    }

    @PatchMapping("/{storeId}/status")
    public ResponseEntity<StoreStatusResponseDto> updateStoreStatus(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody @Valid StoreStatusUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok(ownerStoreService.updateStoreStatus(authUser, storeId, requestDto));
    }

    @PatchMapping("/{storeId}/notice")
    public ResponseEntity<StoreNoticeResponseDto> updateStoreStatus(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody @Valid StoreNoticeUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok(ownerStoreService.updateStoreNotice(authUser, storeId, requestDto));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @Auth AuthUser authUser,
            @PathVariable Long storeId
    ) {
        ownerStoreService.deleteStore(authUser, storeId);
        return ResponseEntity.ok().build();
    }
}
