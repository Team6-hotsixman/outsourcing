package com.example.outsourcing.domain.menu.menuoption.controller;

import com.example.outsourcing.domain.menu.menuoption.dto.request.MenuOptionSaveRequestDto;
import com.example.outsourcing.domain.menu.menuoption.dto.request.MenuOptionUpdateRequestDto;
import com.example.outsourcing.domain.menu.menuoption.dto.response.MenuOptionResponseDto;
import com.example.outsourcing.domain.menu.menuoption.service.MenuOptionService;
import com.example.outsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuOptionController {

    private final MenuOptionService menuOptionService;

    @PostMapping("/options")
    public ResponseEntity<MenuOptionResponseDto> saveMenuOption(
            User user,
            @RequestBody MenuOptionSaveRequestDto requestDto) {
        return ResponseEntity.ok(menuOptionService.saveMenuOption(user, requestDto));
    }

    @PatchMapping("/options/{optionId}")
    public ResponseEntity<MenuOptionResponseDto> updateMenuOption(
            User user,
            @PathVariable Long optionId,
            @RequestBody MenuOptionUpdateRequestDto requestDto) {
        return ResponseEntity.ok(menuOptionService.updateMenuOption(optionId, user, requestDto));
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Void> deleteMenuOption(User user, @PathVariable Long optionId) {
        menuOptionService.deleteMenuOption(optionId, user);
        return ResponseEntity.noContent().build();
    }
}
