package com.kioga.kioga_api_rest.dto.subcategory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubcategoryDto {
  private Long id;
  private String name;
  private String slug;
}
