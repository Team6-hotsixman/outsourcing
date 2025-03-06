package com.example.outsourcing.domain.menu.menuoption.controller;

import com.example.outsourcing.domain.common.annotation.Owner;
import com.example.outsourcing.domain.menu.menuoption.dto.request.MenuOptionSaveRequestDto;
import com.example.outsourcing.domain.menu.menuoption.dto.request.MenuOptionUpdateRequestDto;
import com.example.outsourcing.domain.menu.menuoption.dto.response.MenuOptionResponseDto;
import com.example.outsourcing.domain.menu.menuoption.service.MenuOptionService;
import com.example.outsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuOptionController {

    private final MenuOptionService menuOptionService;

    @Owner
    @PostMapping("/options")
    public ResponseEntity<MenuOptionResponseDto> saveMenuOption(
            @RequestBody MenuOptionSaveRequestDto requestDto) {
        return ResponseEntity.ok(menuOptionService.saveMenuOption(requestDto));
    }

    @GetMapping("/options")
    public ResponseEntity<List<MenuOptionResponseDto>> getMenuOptions() {
        return ResponseEntity.ok(menuOptionService.getMenuOptions());
    }

    @Owner
    @PatchMapping("/options/{optionId}")
    public ResponseEntity<MenuOptionResponseDto> updateMenuOption(
            @PathVariable Long optionId,
            @RequestBody MenuOptionUpdateRequestDto requestDto) {
        return ResponseEntity.ok(menuOptionService.updateMenuOption(optionId, requestDto));
    }

    @Owner
    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Void> deleteMenuOption(@PathVariable Long optionId) {
        menuOptionService.deleteMenuOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
