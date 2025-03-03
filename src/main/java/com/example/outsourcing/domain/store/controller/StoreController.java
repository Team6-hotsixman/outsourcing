package com.example.outsourcing.domain.store.controller;

import com.example.outsourcing.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreNoticeResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreStatusResponseDto;
import com.example.outsourcing.domain.store.service.StoreService;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StoreController {
    /* hyen ho start */
    private final StoreService storeService;

    @PostMapping("/owners/stores")
    public ResponseEntity<StoreResponseDto> saveStore(@RequestBody @Valid StoreSaveRequestDto requestDto) {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        return new ResponseEntity<>(storeService.saveStore(authUser,requestDto), HttpStatus.CREATED);
    }

    @PatchMapping("owners/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @PathVariable Long storeId,
            @RequestBody @Valid StoreUpdateRequestDto requestDto)
    {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        return new ResponseEntity<>(storeService.updateStore(storeId, authUser, requestDto), HttpStatus.OK);
    }

    @PatchMapping("owners/stores/{storeId}/status")
    public ResponseEntity<StoreStatusResponseDto> updateStoreStatus(
            @PathVariable Long storeId,
            @RequestBody @Valid StoreStatusUpdateRequestDto requestDto
    ) {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        return new ResponseEntity<>(storeService.updateStoreStatus(authUser, storeId, requestDto), HttpStatus.OK);
    }

    @PatchMapping("owners/stores/{storeId}/notice")
    public ResponseEntity<StoreNoticeResponseDto> updateStoreStatus(
            @PathVariable Long storeId,
            @RequestBody @Valid StoreNoticeResponseDto requestDto
    ) {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        return new ResponseEntity<>(storeService.updateStoreNotice(authUser, storeId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("owners/stores/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable Long storeId,
            @RequestBody @Valid StoreDeleteRequestDto requestDto
    ) {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        storeService.deleteStore(authUser, storeId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

     /* hyen ho end */
}
