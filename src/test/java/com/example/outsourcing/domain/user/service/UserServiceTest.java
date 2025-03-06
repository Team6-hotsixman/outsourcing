package com.example.outsourcing.domain.user.service;

import com.example.outsourcing.domain.auth.config.PasswordEncoder;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.user.dto.request.UserDeleteRequestDto;
import com.example.outsourcing.domain.user.dto.request.UserUpdateRequestDto;
import com.example.outsourcing.domain.user.dto.response.UserResponseDto;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserStatus;
import com.example.outsourcing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private User user;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;


    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("a@a.com")
                .password("encodedPassword")
                .name("user")
                .point(100)
                .userRole(null)
                .userStatus(null)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
    }
    @Test
    void getUser_유저프로필조회성공() {
        // given
        given(userRepository.findByIdOrElseThrow(anyLong())).willReturn(user);

        // when
        UserResponseDto response = userService.getUser(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(user.getId());

        }

    @Test
    void getUser_유저프로필조회실패_삭제된유저() {
        // given
        user.UpdateUserStatus(UserStatus.DELETE);
        given(userRepository.findByIdOrElseThrow(anyLong())).willReturn(user);

        //when
        // when & then
        assertThatThrownBy(() -> userService.getUser(1L))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorCode.USER_STATUS_DELETE.getMessage());
    }

    @Test
    void passwordUpdate_비밀번호수정성공() {
        // given
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto("oldPassword", "newPassword");
        given(userRepository.findByIdOrElseThrow(anyLong())).willReturn(user);
        given(passwordEncoder.matches("oldPassword", user.getPassword())).willReturn(true);
        given(passwordEncoder.matches("newPassword", user.getPassword())).willReturn(false);
        given(passwordEncoder.encode("newPassword")).willReturn("encodedNewPassword");

        // when
        UserResponseDto response = userService.passwordUpdate(1L, requestDto);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    void passwordUpdate_비밀번호수정실패_중복된비밀번호() {
        // given
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto("oldPassword", "newPassword");
        given(userRepository.findByIdOrElseThrow(anyLong())).willReturn(user);
        given(passwordEncoder.matches("newPassword", user.getPassword())).willReturn(true);

        // when & then
        assertThrows(ApplicationException.class,
                ()-> userService.passwordUpdate(1l,requestDto)
                ,ErrorCode.DUPLICATE_PASSWORD.getMessage());
    }

    @Test
    void deleteUser_유저삭제성공() {// given
        UserDeleteRequestDto requestDto = new UserDeleteRequestDto("password");
        given(userRepository.findByIdOrElseThrow(anyLong())).willReturn(user);
        given(passwordEncoder.matches("password", user.getPassword())).willReturn(true);

        // when
        userService.deleteUser(1L, requestDto);

        // then
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.DELETE);
    };


    @Test
    void deleteUser_유저삭제실패_비밀번호가다름() {
        // given
        UserDeleteRequestDto requestDto = new UserDeleteRequestDto("password");
        given(userRepository.findByIdOrElseThrow(anyLong())).willReturn(user);
        given(passwordEncoder.matches("password", user.getPassword())).willReturn(true);

        // when
        userService.deleteUser(1L, requestDto);

        // then
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.DELETE);
    }
}
