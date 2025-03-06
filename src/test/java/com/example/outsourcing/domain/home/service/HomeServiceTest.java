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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {
    @Mock
    private StoreService storeService;
    @Mock
    private UserAddressService userAddressService;
    @Mock
    private KaKaoMapApiService kaKaoMapApiService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private SearchKeywordRankingService searchKeywordRankingService;
    @InjectMocks
    private HomeService homeService;

    @Test
    void getHomeInfo_홈정보조회성공() {
        //given
        UserAddressResponse address = new UserAddressResponse(1L, "address", true);
        Point location =mock(Point.class);
        List<String> topSearch = List.of("top1", "top2");
        CategoryResponse categoryResponse = new CategoryResponse(1L, "category1");
        StoreResponseDto nearStore = mock(StoreResponseDto.class);
        StoreResponseDto topSellerStore = mock(StoreResponseDto.class);

        given(nearStore.getStoreName()).willReturn("near store");
        given(topSellerStore.getStoreName()).willReturn("top seller store");

        given(userAddressService.getDefaultUserAddress(anyLong())).willReturn(address);
        given(kaKaoMapApiService.getPoint(anyString())).willReturn(location);
        given(searchKeywordRankingService.getTop10SearchKeyword()).willReturn(topSearch);
        given(categoryService.getCategories()).willReturn(List.of(categoryResponse));
        given(storeService.findNearStore(anyInt(), any(Point.class))).willReturn(List.of(nearStore));
        given(storeService.findTopSellerStores(anyInt(), any(Point.class))).willReturn(List.of(topSellerStore));
        //when

        HomeResponse homeInfo = homeService.getHomeInfo(1L);
        //then
        assertEquals(homeInfo.getCategories().size(), 1);
        assertEquals(homeInfo.getNearStore().size(), 1);
        assertEquals(homeInfo.getTopSellerStores().size(), 1);
        assertEquals(homeInfo.getTopSearched().size(), 2);

        assertEquals(homeInfo.getNearStore().get(0).getStoreName(), "near store");
        assertEquals(homeInfo.getTopSellerStores().get(0).getStoreName(), "top seller store");
    }

}