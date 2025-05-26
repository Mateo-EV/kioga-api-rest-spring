package com.kioga.kioga_api_rest.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.kioga.kioga_api_rest.dto.product.ProductBasicDto;
import com.kioga.kioga_api_rest.dto.product.ProductDto;
import com.kioga.kioga_api_rest.entities.Product;

@Mapper(componentModel = "spring", uses = { CategoryMapper.class, BrandMapper.class })
public interface ProductMapper {
  public ProductDto toDto(Product product);

  public List<ProductDto> toDto(List<Product> products);

  public List<ProductBasicDto> toBasicDto(List<Product> products);
}
