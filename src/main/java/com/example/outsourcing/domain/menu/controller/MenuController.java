package com.example.outsourcing.domain.menu.controller;

import com.example.outsourcing.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcing.domain.menu.service.MenuService;
import com.example.outsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/menus")
    public ResponseEntity<MenuResponseDto> saveMenu(User user, @RequestBody MenuSaveRequestDto requestDto) {
        return ResponseEntity.ok(menuService.saveMenu(user, requestDto));
    }

    @PatchMapping("/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long menuId,
            User user,
            @RequestBody MenuUpdateRequestDto requestDto) {
        return ResponseEntity.ok(menuService.updateMenu(menuId, user, requestDto));
    }

    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }
}
