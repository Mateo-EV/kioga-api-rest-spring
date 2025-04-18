package com.kioga.kioga_api_rest.mappers;

import org.mapstruct.Mapper;

import com.kioga.kioga_api_rest.dto.SubcategoryDto;
import com.kioga.kioga_api_rest.entites.Subcategory;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
  SubcategoryDto toDto(Subcategory subcategory);
}
