package com.kioga.kioga_api_rest.services;

import java.util.List;

import com.kioga.kioga_api_rest.dto.category.CategoryDto;
import com.kioga.kioga_api_rest.dto.category.CategoryWithProductsDto;

public interface CategoryService {
  List<CategoryDto> getAllCategories();

  List<CategoryWithProductsDto> getTopCategories();
}
