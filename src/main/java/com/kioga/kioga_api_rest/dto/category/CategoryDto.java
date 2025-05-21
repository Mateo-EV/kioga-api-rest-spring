package com.kioga.kioga_api_rest.dto.category;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDto {
  private Long id;
  private String name;
  private String slug;
  private String image;
}
