package com.example.outsourcing.domain.menu.menuoption.controller;

import com.example.outsourcing.domain.menu.menuoption.service.MenuOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuOptionController {

    private final MenuOptionService menuOptionService;
}
