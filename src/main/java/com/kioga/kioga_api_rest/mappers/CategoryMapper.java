package com.kioga.kioga_api_rest.mappers;

import org.mapstruct.Mapper;

import com.kioga.kioga_api_rest.dto.CategoryDto;
import com.kioga.kioga_api_rest.entites.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryDto toDto(Category category);
}
