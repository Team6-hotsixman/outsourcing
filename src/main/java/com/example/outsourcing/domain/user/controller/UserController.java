package com.example.outsourcing.domain.user.controller;

import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.user.dto.request.UserDeleteRequestDto;
import com.example.outsourcing.domain.user.dto.request.UserUpdateRequestDto;
import com.example.outsourcing.domain.user.dto.response.UserResponseDto;
import com.example.outsourcing.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> passwordUpdate(@Auth AuthUser authUser, @Valid @RequestBody UserUpdateRequestDto userUpdateRequest) {
        return ResponseEntity.ok(userService.passwordUpdate(authUser.getId(), userUpdateRequest));
    }

    @PutMapping("/delete")
    public ResponseEntity<Void> deleteUser(@Auth AuthUser authUser, @Valid @RequestBody UserDeleteRequestDto userDeleteRequest) {
        userService.deleteUser(authUser.getId(), userDeleteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
