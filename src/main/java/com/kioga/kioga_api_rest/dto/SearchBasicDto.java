package com.kioga.kioga_api_rest.dto;

import java.util.List;

import com.kioga.kioga_api_rest.dto.brand.BrandDto;
import com.kioga.kioga_api_rest.dto.category.CategoryDto;
import com.kioga.kioga_api_rest.dto.product.ProductBasicDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchBasicDto {
  private List<ProductBasicDto> products;
  private List<CategoryDto> categories;
  private List<BrandDto> brands;
}
