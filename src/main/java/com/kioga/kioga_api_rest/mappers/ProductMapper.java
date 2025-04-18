package com.kioga.kioga_api_rest.mappers;

import org.mapstruct.Mapper;

import com.kioga.kioga_api_rest.dto.product.ProductDto;
import com.kioga.kioga_api_rest.entites.Product;

@Mapper(componentModel = "spring", uses = { CategoryMapper.class, BrandMapper.class })
public interface ProductMapper {
  public ProductDto toDto(Product product);
}
