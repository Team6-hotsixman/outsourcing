package com.example.outsourcing.domain.store.controller;

import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.*;
import com.example.outsourcing.domain.store.enums.OrderBy;
import com.example.outsourcing.domain.store.service.StoreService;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StoreController {
    /* hyen ho start */
    private final StoreService storeService;


    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(storeService.getStoreAndMenu(storeId));
    }

     /* hyen ho end */
    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponseDto>> searchStore(@Auth AuthUser authUser,
                                                              @RequestParam(name = "searchKeyword", required = false) String searchKeyword,
                                                              @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                              @RequestParam(required = false, defaultValue = "10") Integer size,
                                                              @RequestParam(required = false, defaultValue = "distance") String orderBy) {
        Pageable pageable = PageRequest.of(page-1, size);
        List<StoreResponseDto> stores = storeService.searchStore(authUser.getId(), searchKeyword, pageable, OrderBy.valueOf(orderBy.toUpperCase()));

        return ResponseEntity.ok(stores);
    }

    @GetMapping("/stores/categories/{categoryId}")
    public ResponseEntity<List<StoreResponseDto>> searchStoreByCategory(@Auth AuthUser authUser,
                                                                        @PathVariable long categoryId,
                                                                        @RequestParam(required = false, defaultValue = "10") int size,
                                                                        @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                                        @RequestParam(required = false, defaultValue = "distance") String orderBy){
        Pageable pageable = PageRequest.of(page-1, size);
        List<StoreResponseDto> stores = storeService.searchStoreByCategory(authUser.getId(), categoryId, pageable, OrderBy.valueOf(orderBy.toUpperCase()));

        return ResponseEntity.ok(stores);
    }
}
