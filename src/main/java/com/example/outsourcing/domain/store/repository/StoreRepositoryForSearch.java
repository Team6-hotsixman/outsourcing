package com.example.outsourcing.domain.store.repository;

import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.enums.OrderBy;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepositoryForSearch {
    List<StoreResponseDto> findStoresByArea(Point area, Pageable page, OrderBy orderBy);
    List<StoreResponseDto> findStoresByCategory(Point area, Long category, Pageable page, OrderBy orderBy);
    List<StoreResponseDto> findStoresBySearch(Point area, String search, Pageable page, OrderBy orderBy);
}
