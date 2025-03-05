package com.example.outsourcing.domain.favorite.repository;

import com.example.outsourcing.domain.favorite.entity.Favorite;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {


    default void toggleFavorite(Store store, User user) {
        if (existsByStoreIdAndUserId(store.getId(), user.getId())) {
            deleteByStoreIdAndUserId(store.getId(), user.getId());
        } else {
            save(Favorite.builder()
                    .user(user)
                    .store(store)
                    .build());
        }
    }
    boolean existsByStoreIdAndUserId(Long storeId, Long userId);

    void deleteByStoreIdAndUserId(Long storeId, Long userId);

    @Query ("SELECT f.store " +
            "FROM Favorite f " +
            "WHERE f.user.id = :userId"
    )
    List<Store> findStoresByUserId(Long userId);
}
