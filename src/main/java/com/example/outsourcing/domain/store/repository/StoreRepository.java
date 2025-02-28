package com.example.outsourcing.domain.store.repository;

import com.example.outsourcing.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
