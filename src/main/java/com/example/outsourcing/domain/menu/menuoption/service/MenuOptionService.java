package com.example.outsourcing.domain.menu.menuoption.service;

import com.example.outsourcing.domain.menu.menuoption.repository.MenuOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;
}
