package com.example.outsourcing.domain.home.service;

import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.common.service.KaKaoMapApiService;
import com.example.outsourcing.domain.common.service.SearchKeywordRankingService;
import com.example.outsourcing.domain.home.dto.HomeResponse;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.service.StoreService;
import com.example.outsourcing.domain.user.dto.response.UserAddressResponse;
import com.example.outsourcing.domain.user.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final StoreService storeService;
    private final UserAddressService userAddressService;
    private final KaKaoMapApiService kaKaoMapApiService;
    private final CategoryService categoryService;
    private final SearchKeywordRankingService searchKeywordRankingService;

    public HomeResponse getHomeInfo(Long userId) {
        UserAddressResponse defaultUserAddress = userAddressService.getDefaultUserAddress(userId);
        Point point = kaKaoMapApiService.getPoint(defaultUserAddress.getAddress());

        List<String> top10SearchKeyword = searchKeywordRankingService.getTop10SearchKeyword();
        List<CategoryResponse> allCategories = categoryService.getCategories();
        List<StoreResponseDto> nearStore = storeService.findNearStore(5, point);
        List<StoreResponseDto> topSellerStores = storeService.findTopSellerStores(5, point);

        return new HomeResponse(top10SearchKeyword, allCategories, nearStore, topSellerStores);
    }
}
