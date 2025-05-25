package com.kioga.kioga_api_rest.dto.brand;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrandDto {
  private Long id;
  private String name;
  private String slug;
  private String image;
}