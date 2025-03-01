package com.example.outsourcing.domain.store.service;


import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.common.dto.LatLng;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.exception.StoreLimitExceededException;
import com.example.outsourcing.domain.common.service.GeoCodingService;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.common.service.SearchKeywordRankingService;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.entity.UserAddress;
import com.example.outsourcing.domain.user.enums.AddressStatus;
import com.example.outsourcing.domain.user.repository.UserAddressRepository;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    private final CategoryService categoryService;

    private final ImageService imageService;

    private final UserRepository userRepository;

    private final GeoCodingService geoCodingService;
    private final UserAddressRepository userAddressRepository;
    private final SearchKeywordRankingService searchKeywordRankingService;

    /* hyen ho start */
    @Transactional
    public StoreResponseDto saveStore(User authUser, StoreSaveRequestDto dto) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(dto.getCatogoryId());
        Image image = imageService.getImageById(dto.getImageId());
        User user = userRepository.findById(1L).orElseThrow();

        //주소로 위도 경도 가져온다
        LatLng latLng = geoCodingService.getLatLng(dto.getAddress());
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
        Point location = geometryFactory.createPoint(new Coordinate(latLng.getLongitude(), latLng.getLatitude()));

        if (storeRepository.countStoresByUserId(user.getId()) >= 3) {
            throw new StoreLimitExceededException();
        }

        Store store = Store.builder()
                .user(user)
                .image(image)
                .category(new Category(categoryResponse))
                .storeName(dto.getStoreName())
                .storeStatus(dto.getStoreStatus())
                .storeNotice(dto.getStoreNotice())
                .address(dto.getAddress())
                .minOrderPrice(dto.getMinOrderPrice())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .location(location)
                .build();
        storeRepository.save(store);
        return StoreResponseDto.of(store);
    }


    /* hyen ho end */

    public List<StoreResponseDto> getAllStores(long userId, String searchKeyword) {
        //사용자의 기본 배송지를 가져온다
        UserAddress address = userAddressRepository.findByUserIdAndAddressStatus(userId, AddressStatus.DEFAULT)
                .orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_DEFAULT_ADDRESS));

        //배송지에서 위도, 경도 값을 가져온다
        LatLng latLng = geoCodingService.getLatLng(address.getAddress());

        //반경 3km이내의 가게리스트를 가져온다.
        List<Store> stores = storeRepository.findStoresByWithinRadius(latLng.getLongitude(), latLng.getLatitude(), 3000, searchKeyword);

        //검색결과가 존재하고 검색어가 null이 아니라면 redis에 검색어 등록
        if(searchKeyword != null && !stores.isEmpty()){
            searchKeywordRankingService.increaseCount(searchKeyword);
        }

        return stores.stream().map(StoreResponseDto::of).toList();
    }
}
