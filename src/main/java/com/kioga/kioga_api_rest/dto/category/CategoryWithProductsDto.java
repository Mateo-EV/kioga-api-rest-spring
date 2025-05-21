package com.kioga.kioga_api_rest.dto.category;

import java.util.List;

import com.kioga.kioga_api_rest.dto.product.ProductDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryWithProductsDto {
  private Long id;
  private String name;
  private String slug;
  private String image;
  private List<ProductDto> products;
}
