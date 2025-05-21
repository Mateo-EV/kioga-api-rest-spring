package com.kioga.kioga_api_rest.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.kioga.kioga_api_rest.dto.category.CategoryDto;
import com.kioga.kioga_api_rest.dto.category.CategoryWithProductsDto;
import com.kioga.kioga_api_rest.entites.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryDto toDto(Category category);

  List<CategoryDto> toDto(List<Category> category);

  List<CategoryWithProductsDto> toWithProductsDto(List<Category> category);
}
