package com.example.outsourcing.domain.user.service;


import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.service.KaKaoMapApiService;
import com.example.outsourcing.domain.user.dto.request.UserAddressCreateDto;
import com.example.outsourcing.domain.user.dto.request.UserAddressUpdateRequestDto;
import com.example.outsourcing.domain.user.dto.response.UserAddressResponse;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.entity.UserAddress;
import com.example.outsourcing.domain.user.enums.AddressStatus;
import com.example.outsourcing.domain.user.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressService {
    private final UserAddressRepository userAddressRepository;
    private final KaKaoMapApiService kaKaoMapApiService;

    public List<UserAddressResponse> getUserAddresses(AuthUser authUser) {
        List<UserAddress> addresses = userAddressRepository.findByUser(User.fromAuthUser(authUser));

        return addresses.stream().map(UserAddressResponse::of).toList();
    }

    @Transactional
    public UserAddressResponse saveUserAddress(AuthUser authUser, UserAddressCreateDto userAddressCreateDto) {
        // 좌표값을 가져올 수 없으면 잘못된 주소임
        kaKaoMapApiService.getLatLng(userAddressCreateDto.getAddress());

        if(userAddressRepository.existsByUserIdAndAddress(authUser.getId(), userAddressCreateDto.getAddress())) {
            throw new ApplicationException(ErrorCode.DUPLICATE_ADDRESS);
        }
        User user = User.fromAuthUser(authUser);
        UserAddress userAddress = UserAddress.builder()
                .addressStatus(AddressStatus.ANOTHER) //일반 상태로 주소를 저장한다
                .user(user)
                .address(userAddressCreateDto.getAddress())
                .build();

        UserAddress save = userAddressRepository.save(userAddress);

        return UserAddressResponse.of(save);
    }

    public UserAddressResponse getDefaultUserAddress(long userId) {
        UserAddress userAddress = userAddressRepository.findByUserIdAndAddressStatus(userId, AddressStatus.DEFAULT)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_DEFAULT_ADDRESS));
        return UserAddressResponse.of(userAddress);
    }

    @Transactional
    public UserAddressResponse updateUserAddress(AuthUser authUser, Long userAddressId, UserAddressUpdateRequestDto userAddressUpdateRequestDto) {
        // 좌표값을 가져올 수 없으면 잘못된 주소임
        kaKaoMapApiService.getLatLng(userAddressUpdateRequestDto.getNewAddress());

        UserAddress userAddress = userAddressRepository.findByIdWithUser(userAddressId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_ADDRESS));
        if(!userAddress.getUser().getId().equals(authUser.getId())){
            throw new ApplicationException(ErrorCode.Unauthorized_User);
        }

        if(userAddressRepository.existsByUserIdAndAddress(authUser.getId(), userAddressUpdateRequestDto.getNewAddress())){
            throw new ApplicationException(ErrorCode.DUPLICATE_ADDRESS);
        }

        userAddress.updateAddress(userAddressUpdateRequestDto.getNewAddress());

        return UserAddressResponse.of(userAddress);
    }

    @Transactional
    public UserAddressResponse setDefaultUserAddress(AuthUser authUser, Long userAddressId) {

        UserAddress userAddress = userAddressRepository.findByIdWithUser(userAddressId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_ADDRESS));
        if(!userAddress.getUser().getId().equals(authUser.getId())){
            throw new ApplicationException(ErrorCode.Unauthorized_User);
        }

        //기존 기본 주소는 다시 일반 상태로 되돌린다
        userAddressRepository.findByUserIdAndAddressStatus(authUser.getId(), AddressStatus.DEFAULT).ifPresent(UserAddress::setAnother);

        userAddress.setDefault();

        return UserAddressResponse.of(userAddress);
    }

    public void deleteUserAddress(AuthUser authUser, Long userAddressId) {
        UserAddress userAddress = userAddressRepository.findByIdWithUser(userAddressId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_ADDRESS));
        if(!userAddress.getUser().getId().equals(authUser.getId())){
            throw new ApplicationException(ErrorCode.Unauthorized_User);
        }
        userAddressRepository.deleteById(userAddressId);
    }
}
