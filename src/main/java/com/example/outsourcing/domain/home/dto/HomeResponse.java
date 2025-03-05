package com.example.outsourcing.domain.home.dto;

import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HomeResponse {
    List<String> topSearched;
    List<CategoryResponse> categories;
    List<StoreResponseDto> nearStore;
    List<StoreResponseDto> topSellerStores;
}
