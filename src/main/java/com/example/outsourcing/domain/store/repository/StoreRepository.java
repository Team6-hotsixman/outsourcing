package com.example.outsourcing.domain.store.repository;

import com.example.outsourcing.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryForSearch {

    @Query("SELECT COUNT(s) " +
            "FROM Store s " +
            "WHERE s.user.id = :userId")
    long countStoresByUserId(Long userId);
}
