package com.example.outsourcing.domain.home.controller;


import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.home.dto.HomeResponse;
import com.example.outsourcing.domain.home.service.HomeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;


    @GetMapping("/home")
    public ResponseEntity<HomeResponse> home(@Auth AuthUser authUser) {
        HomeResponse homeInfo = homeService.getHomeInfo(authUser.getId());

        return ResponseEntity.ok(homeInfo);
    }
}
