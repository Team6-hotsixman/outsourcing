package com.example.outsourcing.domain.menu.menuoption.repository;

import com.example.outsourcing.domain.menu.menuoption.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {
}
