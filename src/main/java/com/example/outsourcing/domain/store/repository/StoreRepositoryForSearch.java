package com.example.outsourcing.domain.store.repository;

import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseForNativeQuery;
import com.example.outsourcing.domain.store.enums.OrderBy;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepositoryForSearch {
    List<StoreResponseDto> findStoresByArea(Point area, Pageable page, OrderBy orderBy, OrderStatus orderStatus, StoreStatus storeStatus);
    List<StoreResponseDto> findStoresByCategory(Point area, Long category, Pageable page, OrderBy orderBy, OrderStatus orderStatus, StoreStatus storeStatus);
    //가장 많이판 가게
    List<StoreResponseDto> findTopSellerStores(int top, Point location, OrderStatus orderStatus, StoreStatus storeStatus);
    //가장 가까운 가게
    List<StoreResponseDto> findTopNearStores(int top, Point location, OrderStatus orderStatus, StoreStatus storeStatus);
}
