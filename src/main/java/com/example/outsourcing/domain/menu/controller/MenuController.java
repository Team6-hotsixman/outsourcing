package com.example.outsourcing.domain.menu.controller;

import com.example.outsourcing.domain.common.annotation.Owner;
import com.example.outsourcing.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcing.domain.menu.service.MenuService;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Owner
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/menus")
    public ResponseEntity<MenuResponseDto> saveMenu(
            @RequestPart(value = "json") @Valid MenuSaveRequestDto requestDto,
            @RequestPart(value = "file") MultipartFile file)
    {
        return ResponseEntity.ok(menuService.saveMenu(requestDto, file));
    }

    @PatchMapping("/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long menuId,
            @RequestPart(value = "json") @Valid MenuUpdateRequestDto requestDto,
            @RequestPart(value = "file") MultipartFile file)
     {
        return ResponseEntity.ok(menuService.updateMenu(menuId, requestDto, file));
    }

    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }
}
