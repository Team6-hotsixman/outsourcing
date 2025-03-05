package com.example.outsourcing.domain.store.controller;

import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.enums.OrderBy;
import com.example.outsourcing.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(storeService.getStoreAndMenu(storeId));
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponseDto>> searchStore(@RequestParam(name = "searchKeyword", required = false) String searchKeyword,
                                                              @RequestParam(required = false, defaultValue = "10") int size,
                                                              @RequestParam(required = false, defaultValue = "distance") String orderBy) {
        long userId = 1L;
        List<StoreResponseDto> stores = storeService.searchStore(userId, searchKeyword, size, OrderBy.valueOf(orderBy.toUpperCase()));

        return ResponseEntity.ok(stores);
    }

    @GetMapping("/stores/categories/{categoryId}")
    public ResponseEntity<List<StoreResponseDto>> searchStoreByCategory(@PathVariable long categoryId,
                                                                        @RequestParam(required = false, defaultValue = "10") int size,
                                                                        @RequestParam(required = false, defaultValue = "distance") String orderBy){
        long userId = 1L;
        List<StoreResponseDto> stores = storeService.searchStoreByCategory(userId, categoryId, size, OrderBy.valueOf(orderBy.toUpperCase()));

        return ResponseEntity.ok(stores);
    }
}
