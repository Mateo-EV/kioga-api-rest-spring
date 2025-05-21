package com.kioga.kioga_api_rest.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.kioga.kioga_api_rest.dto.BrandDto;
import com.kioga.kioga_api_rest.entites.Brand;

@Mapper(componentModel = "spring")
public interface BrandMapper {
  BrandDto toDto(Brand brand);

  List<BrandDto> toDto(List<Brand> brand);
}
