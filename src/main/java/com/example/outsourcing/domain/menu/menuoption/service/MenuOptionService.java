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

@Service
@RequiredArgsConstructor
public class MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;
    private final MenuRepository menuRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public MenuOptionResponseDto saveMenuOption(User user, MenuOptionSaveRequestDto requestDto) {
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_MENU));

        // 권한 검증 - 추후 AOP 분리
        if (menu.getStore().getUser().getId().equals(user.getId())) {
            throw new UnauthorizedUserException();
        }

        Image image = imageRepository.findById(requestDto.getImageId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_IMAGE));

        MenuOption menuOption = MenuOptionSaveRequestDto.toEntity(requestDto, menu, image);

        menuOptionRepository.save(menuOption);

        return MenuOptionResponseDto.of(menuOption);
    }

    @Transactional
    public MenuOptionResponseDto updateMenuOption(Long optionId, User user, MenuOptionUpdateRequestDto requestDto) {
        MenuOption menuOption = menuOptionRepository.findById(optionId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_MENU_OPTION));

        // 권한 검증 - 추후 AOP 분리
        if (menuOption.getMenu().getStore().getUser().getId().equals(user.getId())) {
            throw new UnauthorizedUserException();
        }

        Image image = imageRepository.findById(requestDto.getImageId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_IMAGE));

        menuOption.updateMenuOption(requestDto, image);

        return MenuOptionResponseDto.of(menuOption);
    }

    @Transactional
    public void deleteMenuOption(Long optionId, User user) {
        MenuOption menuOption = menuOptionRepository.findById(optionId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_MENU_OPTION));

        // 권한 검증 - 추후 AOP 분리
        if (menuOption.getMenu().getStore().getUser().getId().equals(user.getId())) {
            throw new UnauthorizedUserException();
        }

        menuOptionRepository.delete(menuOption);
    }
}
