package com.example.outsourcing.domain.favorite.controller;

import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.favorite.service.FavoriteService;
import com.example.outsourcing.domain.store.dto.response.StoreListResponseDto;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class FavoriteController {
    private final FavoriteService favoriteService;

    // 즐겨찾기 기능 (토글형)
    @PostMapping("/{storeId}/favorits")
    public ResponseEntity<Void> toggleFavorites(@PathVariable Long storeId, @Auth AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        favoriteService.toggleFavorite(storeId, user);
        return ResponseEntity.ok().build();
    }

    // 즐겨찾기한 가게 리스트 반환
    @GetMapping("/favorits")
    public ResponseEntity<List<StoreListResponseDto>> getFavorites(@Auth AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(favoriteService.getFavorites(user));
    }
}
