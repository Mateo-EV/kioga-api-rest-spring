package com.kioga.kioga_api_rest.dto.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBasicDto {
  private Long id;
  private String name;
  private String slug;
  private BigDecimal price;
  private BigDecimal discount;
  private String image;
  private Integer stock;

}
