package com.kioga.kioga_api_rest.mappers;

import org.mapstruct.Mapper;

import com.kioga.kioga_api_rest.dto.subcategory.SubcategoryDto;
import com.kioga.kioga_api_rest.entities.Subcategory;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
  SubcategoryDto toDto(Subcategory subcategory);
}
