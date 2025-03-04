package com.example.outsourcing.domain.store.repository;

import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.enums.OrderBy;
import org.locationtech.jts.geom.Point;

import java.util.List;

public interface StoreRepositoryForSearch {
    List<StoreResponseDto> findStoresByArea(Point area, int size, OrderBy orderBy);
    List<StoreResponseDto> findStoresByCategory(Point area, Long category, int size, OrderBy orderBy);
    List<StoreResponseDto> findStoresBySearch(Point area, String search, int size, OrderBy orderBy);
}
