package com.example.outsourcing.domain.store.controller;

import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.service.StoreService;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        return new ResponseEntity<>(storeService.saveStore(authUser, requestDto), HttpStatus.OK);
    }

     /* hyen ho end */
    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponseDto>> getAllStores(@RequestParam(required = false) String searchKeyword) {
        long userId = 1L;
        List<StoreResponseDto> stores = storeService.getAllStores(userId, searchKeyword);

        return ResponseEntity.ok(stores);
    }
}
