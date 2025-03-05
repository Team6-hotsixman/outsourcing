package com.example.outsourcing.domain.favorite.controller;

import com.example.outsourcing.domain.favorite.service.FavoriteService;
import com.example.outsourcing.domain.store.dto.response.StoreListResponseDto;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/stores")
public class FavoriteController {
    private final FavoriteService favoriteService;

    // 즐겨찾기 기능 (토글형)
    @PostMapping("/{storeId}/favorits")
    public ResponseEntity<Void> toggleFavorites(@PathVariable Long storeId) {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        favoriteService.toggleFavorite(storeId, authUser);
        return ResponseEntity.ok().build();
    }

    // 즐겨찾기한 가게 리스트 반환
    @GetMapping("/favorits")
    public ResponseEntity<List<StoreListResponseDto>> getFavorites() {
        User authUser = User.builder()
                .email("qwer@1234")
                .password("password")
                .userRole(UserRole.OWNER)
                .build();
        return ResponseEntity.ok(favoriteService.getFavorites(authUser));
    }
}
