package com.example.outsourcing.domain.menu.menuoption.service;

import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.exception.UnauthorizedUserException;
import com.example.outsourcing.domain.common.repository.ImageRepository;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.menuoption.dto.request.MenuOptionSaveRequestDto;
import com.example.outsourcing.domain.menu.menuoption.dto.request.MenuOptionUpdateRequestDto;
import com.example.outsourcing.domain.menu.menuoption.dto.response.MenuOptionResponseDto;
import com.example.outsourcing.domain.menu.menuoption.entity.MenuOption;
import com.example.outsourcing.domain.menu.menuoption.repository.MenuOptionRepository;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;
    private final MenuRepository menuRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public MenuOptionResponseDto saveMenuOption(MenuOptionSaveRequestDto requestDto) {
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_MENU));

        Image image = imageRepository.findById(requestDto.getImageId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_IMAGE));

        MenuOption menuOption = MenuOptionSaveRequestDto.toEntity(requestDto, menu, image);

        menuOptionRepository.save(menuOption);

        return MenuOptionResponseDto.of(menuOption);
    }

    public List<MenuOptionResponseDto> getMenuOptions() {
        return menuOptionRepository.findAll().stream()
                .map(option -> new MenuOptionResponseDto(
                        option.getOptionName(),
                        option.getPrice(),
                        option.getDescription(),
                        option.getMenu().getMenuName(),
                        option.isAvailable(),
                        option.getImage().getImagePath()
                )).toList();
    }

    @Transactional
    public MenuOptionResponseDto updateMenuOption(Long optionId, MenuOptionUpdateRequestDto requestDto) {
        MenuOption menuOption = menuOptionRepository.findById(optionId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_MENU_OPTION));

        Image image = imageRepository.findById(requestDto.getImageId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_IMAGE));

        menuOption.updateMenuOption(requestDto, image);

        return MenuOptionResponseDto.of(menuOption);
    }

    @Transactional
    public void deleteMenuOption(Long optionId) {
        MenuOption menuOption = menuOptionRepository.findById(optionId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_MENU_OPTION));

        menuOptionRepository.delete(menuOption);
    }
}
