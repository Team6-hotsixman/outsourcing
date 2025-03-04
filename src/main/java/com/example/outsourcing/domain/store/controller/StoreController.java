package com.example.outsourcing.domain.store.controller;

import com.example.outsourcing.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.*;
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


    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto>  getStore(
            @PathVariable Long storeId
    ) {
        return new ResponseEntity<>(storeService.getStore(storeId), HttpStatus.OK);
    }

     /* hyen ho end */
}
