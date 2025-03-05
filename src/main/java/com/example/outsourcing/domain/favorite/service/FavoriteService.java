package com.example.outsourcing.domain.favorite.service;

import com.example.outsourcing.domain.favorite.repository.FavoriteRepository;
import com.example.outsourcing.domain.store.dto.response.StoreListResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.service.StoreService;
import com.example.outsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    private final StoreService storeService;

    public void toggleFavorite(Long storeId, User authUser) {
        Store store = storeService.getStore(storeId);
        favoriteRepository.toggleFavorite(store, authUser);
    }

    public List<StoreListResponseDto> getFavorites(User authUser) {
        return favoriteRepository.findStoresByUserId(authUser.getId()).stream()
                .map(StoreListResponseDto::of)
                .collect(Collectors.toList());
    }
}
