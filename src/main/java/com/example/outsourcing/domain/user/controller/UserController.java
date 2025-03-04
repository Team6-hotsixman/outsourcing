package com.example.outsourcing.domain.user.controller;

import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.user.dto.request.UserDeleteRequestDto;
import com.example.outsourcing.domain.user.dto.request.UserUpdateRequestDto;
import com.example.outsourcing.domain.user.dto.response.UserResponseDto;
import com.example.outsourcing.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/{userid}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable long userid) {
        return ResponseEntity.ok(userService.getUser(userid));
    }

    // 내 정보 수정
    @PutMapping
    public ResponseEntity<UserResponseDto> passwordUpdate(@Auth AuthUser authUser, @Valid @RequestBody UserUpdateRequestDto userUpdateRequest) {
        return ResponseEntity.ok(userService.passwordUpdate(authUser.getId(), userUpdateRequest));
    }

    /*@PutMapping()
    public ResponseEntity<Void> deleteUser(@Auth AuthUser authUser, @Valid @RequestBody UserDeleteRequestDto userDeleteRequest) {
        userService.deleteUser(authUser.getId(), userDeleteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }*/
}
