package com.kioga.kioga_api_rest.dto.product;

import java.math.BigDecimal;

import com.kioga.kioga_api_rest.dto.BrandDto;
import com.kioga.kioga_api_rest.dto.SubcategoryDto;
import com.kioga.kioga_api_rest.dto.category.CategoryDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {
  private Long id;
  private String name;
  private String slug;
  private BigDecimal price;
  private BigDecimal discount;
  private String image;
  private Integer stock;

  private CategoryDto category;
  private SubcategoryDto subcategory;
  private BrandDto brand;
}