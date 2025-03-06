package com.example.outsourcing.domain.auth.controller;

import com.example.outsourcing.domain.auth.dto.request.KakaoUserInfo;
import com.example.outsourcing.domain.auth.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    // 1️⃣ 로그인 URL 반환
    @GetMapping
    public ResponseEntity<String> getKakaoLoginUrl() {
        String loginUrl = kakaoAuthService.getKakaoLoginUrl();
        return ResponseEntity.ok(loginUrl);
    }

    // 2️⃣ 카카오에서 인증 후 Redirect 되는 곳 (인가 코드 받아서 Access Token 요청)
    @GetMapping("/login")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        String accessToken = kakaoAuthService.getAccessToken(code);
        KakaoUserInfo userInfo = kakaoAuthService.getUserInfo(accessToken);

        // ✅ JWT 발급 또는 세션 저장 후 클라이언트로 토큰 반환
        return ResponseEntity.ok()
                .body(Map.of(
                        "message", "로그인 성공",
                        "nickname", userInfo.getNickname(),
                        "accessToken", accessToken
                ));
    }
}

