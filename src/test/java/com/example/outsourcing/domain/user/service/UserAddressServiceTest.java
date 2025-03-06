package com.example.outsourcing.domain.user.service;

import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.dto.LatLng;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.service.KaKaoMapApiService;
import com.example.outsourcing.domain.user.dto.request.UserAddressCreateDto;
import com.example.outsourcing.domain.user.dto.request.UserAddressUpdateRequestDto;
import com.example.outsourcing.domain.user.dto.response.UserAddressResponse;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.entity.UserAddress;
import com.example.outsourcing.domain.user.enums.AddressStatus;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.repository.UserAddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAddressServiceTest {

    @Mock
    private KaKaoMapApiService kaKaoMapApiService;

    @Mock
    private UserAddressRepository userAddressRepository;

    @InjectMocks
    private UserAddressService userAddressService;



    @Test
    void getUserAddress_주소리스트를가져온다() {
        //given
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        User user = createUser(userId, "user@a.com", "user1");
        UserAddress address1 = createUserAddress(1L, "address1", AddressStatus.DEFAULT, user);
        UserAddress address2 = createUserAddress(2L, "address2", AddressStatus.ANOTHER, user);
        given(userAddressRepository.findByUser(any(User.class))).willReturn(List.of(address1,address2));
        //when
        List<UserAddressResponse> userAddresses = userAddressService.getUserAddresses(authUser);
        //then
        assertEquals(2, userAddresses.size());
        assertEquals("address2", userAddresses.get(1).getAddress());
    }

    @Test
    void getUserAddress_빈주소리스트를가져온다() {
        //given
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        given(userAddressRepository.findByUser(any(User.class))).willReturn(List.of());
        //when
        List<UserAddressResponse> userAddresses = userAddressService.getUserAddresses(authUser);
        //then
        assertEquals(0, userAddresses.size());
    }

    @Test
    void saveUserAddress_사용자주소를등록한다(){
        //given
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        User user = createUser(userId, "user@a.com", "user1");
        UserAddressCreateDto userAddressCreateDto = new UserAddressCreateDto("new address");
        UserAddress address = createUserAddress(1L, userAddressCreateDto.getAddress(), AddressStatus.ANOTHER, user);
        given(kaKaoMapApiService.getLatLng(anyString())).willReturn(new LatLng());
        given(userAddressRepository.existsByUserIdAndAddress(anyLong(), anyString())).willReturn(false);
        given(userAddressRepository.save(any(UserAddress.class))).willReturn(address);
        //when
        UserAddressResponse userAddressResponse = userAddressService.saveUserAddress(authUser, userAddressCreateDto);
        //then
        assertEquals("new address", userAddressResponse.getAddress());
        assertFalse(userAddressResponse.isDefault());
    }

    @Test
    void saveUserAddress_잘못된주소를입력했을경우예외를던진다(){
        //given
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        UserAddressCreateDto userAddressCreateDto = new UserAddressCreateDto("new address");
        given(kaKaoMapApiService.getLatLng(anyString())).willThrow(new ApplicationException(ErrorCode.INVALID_ADDRESS));
        //when & then
        assertThrows(ApplicationException.class,
                ()-> userAddressService.saveUserAddress(authUser, userAddressCreateDto),
                ErrorCode.INVALID_ADDRESS.getMessage()) ;
    }

    @Test
    void saveUserAddress_중복된주소를등록하려할때예외를던진다(){
        //given
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        UserAddressCreateDto userAddressCreateDto = new UserAddressCreateDto("new address");
        given(kaKaoMapApiService.getLatLng(anyString())).willReturn(new LatLng());
        given(userAddressRepository.existsByUserIdAndAddress(anyLong(), anyString())).willReturn(true);
        //when & then
        assertThrows(ApplicationException.class,
                ()-> userAddressService.saveUserAddress(authUser, userAddressCreateDto),
                ErrorCode.DUPLICATE_ADDRESS.getMessage()) ;
    }

    @Test
    void getDefaultUserAddress_사용자기본주소를가져온다() {
        //given
        long userId = 1L;
        User user = createUser(userId, "user@a.com", "user1");
        UserAddress address = createUserAddress(1L, "address1", AddressStatus.DEFAULT, user);
        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.of(address));
        //when
        UserAddressResponse defaultUserAddress = userAddressService.getDefaultUserAddress(userId);

        //then
        assertEquals("address1", defaultUserAddress.getAddress());
        assertTrue(defaultUserAddress.isDefault());
    }

    @Test
    void getDefaultUserAddress_사용자기본주소가없을때는예외를던진다() {
        //given
        long userId = 1L;
        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.empty());
        //when & then
        assertThrows(ApplicationException.class,
                ()->userAddressService.getDefaultUserAddress(userId),
                ErrorCode.NOT_FOUND_DEFAULT_ADDRESS.getMessage()) ;
    }

    @Test
    void updateUserAddress_사용자주소를업데이트한다(){
        //given
        long addressId =1L;
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        User user = createUser(userId, "user@a.com", "user1");
        UserAddressUpdateRequestDto userAddressUpdateRequestDto = new UserAddressUpdateRequestDto("new address");
        UserAddress address = createUserAddress(1L, "address", AddressStatus.ANOTHER, user);
        given(kaKaoMapApiService.getLatLng(anyString())).willReturn(new LatLng());
        given(userAddressRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(address));
        given(userAddressRepository.existsByUserIdAndAddress(anyLong(), anyString())).willReturn(false);
        //when
        UserAddressResponse userAddressResponse = userAddressService.updateUserAddress(authUser, addressId, userAddressUpdateRequestDto);
        //then
        assertEquals("new address", userAddressResponse.getAddress());
    }

    @Test
    void updateUserAddress_잘못된주소로업데이트시예외를던진다(){
        //given
        long addressId =1L;
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        UserAddressUpdateRequestDto userAddressUpdateRequestDto = new UserAddressUpdateRequestDto("new address");
        given(kaKaoMapApiService.getLatLng(anyString())).willThrow(new ApplicationException(ErrorCode.INVALID_ADDRESS));
        //when & then
        assertThrows(ApplicationException.class,
                ()-> userAddressService.updateUserAddress(authUser, addressId, userAddressUpdateRequestDto),
                ErrorCode.INVALID_ADDRESS.getMessage());

    }

    @Test
    void updateUserAddress_이미존재하는주소로는업데이트할수없다(){
        //given
        long addressId =1L;
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        User user = createUser(userId, "user@a.com", "user1");
        UserAddressUpdateRequestDto userAddressUpdateRequestDto = new UserAddressUpdateRequestDto("new address");
        UserAddress address = createUserAddress(1L, "address", AddressStatus.ANOTHER, user);
        given(kaKaoMapApiService.getLatLng(anyString())).willReturn(new LatLng());
        given(userAddressRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(address));
        given(userAddressRepository.existsByUserIdAndAddress(anyLong(), anyString())).willReturn(true);
        //when & then
        assertThrows(ApplicationException.class,
                ()-> userAddressService.updateUserAddress(authUser, addressId, userAddressUpdateRequestDto),
                ErrorCode.DUPLICATE_ADDRESS.getMessage());
    }

    @Test
    void updateUserAddress_id로주소를찾을수없으면예외를던진다(){
        //given
        long addressId =1L;
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        UserAddressUpdateRequestDto userAddressUpdateRequestDto = new UserAddressUpdateRequestDto("new address");
        given(kaKaoMapApiService.getLatLng(anyString())).willReturn(new LatLng());
        given(userAddressRepository.findByIdWithUser(anyLong())).willReturn(Optional.empty());
        //when & then
        assertThrows(ApplicationException.class,
                ()-> userAddressService.updateUserAddress(authUser, addressId, userAddressUpdateRequestDto),
                ErrorCode.NOT_FOUND_ADDRESS.getMessage());
    }


    @Test
    void updateUserAddress_자신이등록한주소가아닐경우예외를던진다(){
        //given
        long addressId =1L;
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        User anotherUser = createUser(2L, "user@a.com", "user1");
        UserAddressUpdateRequestDto userAddressUpdateRequestDto = new UserAddressUpdateRequestDto("new address");
        UserAddress address = createUserAddress(1L, "address", AddressStatus.ANOTHER, anotherUser);
        given(kaKaoMapApiService.getLatLng(anyString())).willReturn(new LatLng());
        given(userAddressRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(address));
        //when & then
        assertThrows(ApplicationException.class,
                ()-> userAddressService.updateUserAddress(authUser, addressId, userAddressUpdateRequestDto),
                ErrorCode.Unauthorized_User.getMessage());
    }

    @Test
    void setDefaultUserAddress_주소를기본주소로변경한다(){
        //given
        long addressId =1L;
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        User user = createUser(userId, "user@a.com", "user1");
        UserAddress address = createUserAddress(1L, "address", AddressStatus.ANOTHER, user);
        UserAddress defaultAddress = createUserAddress(2L, "address", AddressStatus.DEFAULT, user);

        given(userAddressRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(address));
        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.of(defaultAddress));
        //when
        UserAddressResponse userAddressResponse = userAddressService.updateUserAddressToDefault(authUser, addressId);
        //then

        assertTrue(userAddressResponse.isDefault());
        assertEquals(defaultAddress.getAddressStatus(), AddressStatus.ANOTHER);
    }

    @Test
    void setDefaultUserAddress_자신이등록하지않은주소는기본주소로변경할수없다(){
        //given
        long addressId =1L;
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        User another = createUser(2L, "user@a.com", "user1");
        UserAddress address = createUserAddress(1L, "address", AddressStatus.ANOTHER, another);

        given(userAddressRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(address));
        //when &bthen
        assertThrows(ApplicationException.class,
                ()-> userAddressService.updateUserAddressToDefault(authUser, addressId),
                ErrorCode.Unauthorized_User.getMessage());
    }

    @Test
    void setDefaultUserAddress_기존의기본주소가없어도오류는발생하지않는다(){
        //given
        long addressId =1L;
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        User user = createUser(userId, "user@a.com", "user1");
        UserAddress address = createUserAddress(1L, "address", AddressStatus.ANOTHER, user);

        given(userAddressRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(address));
        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.empty());
        //when
        UserAddressResponse userAddressResponse = userAddressService.updateUserAddressToDefault(authUser, addressId);
        //then
        assertTrue(userAddressResponse.isDefault());
    }

    @Test
    void deleteUserAddress_상용자주소를삭제한다() {
        //given
        long addressId =1L;
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        User user = createUser(userId, "user@a.com", "user1");
        UserAddress address = createUserAddress(1L, "address", AddressStatus.ANOTHER, user);
        given(userAddressRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(address));
        doNothing().when(userAddressRepository).deleteById(anyLong());
        //when
        userAddressService.deleteUserAddress(authUser, addressId);
        //then
        verify(userAddressRepository, times(1)).deleteById(anyLong());

    }

    @Test
    void deleteUserAddress_자신이등록한주소가아닐경우예외를던진다() {
        //given
        long addressId =1L;
        long userId = 1L;
        AuthUser authUser =new AuthUser(userId, "", UserRole.USER);
        User another = createUser(2L, "user@a.com", "user1");
        UserAddress address = createUserAddress(1L, "address", AddressStatus.ANOTHER, another);
        given(userAddressRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(address));
        //when & then
        assertThrows(ApplicationException.class,
                ()->userAddressService.deleteUserAddress(authUser, addressId),
                ErrorCode.Unauthorized_User.getMessage());

    }


    UserAddress createUserAddress(long id, String address, AddressStatus status, User user) {
        UserAddress userAddress = UserAddress.builder()
                .address(address)
                .addressStatus(status)
                .user(user).build();
        ReflectionTestUtils.setField(userAddress, "id", id);
        return userAddress;
    }

    User createUser(long id, String email, String name) {
        User user = User.builder().email(email).name(name).build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}