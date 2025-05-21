package com.kioga.kioga_api_rest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CursorPaginatedResponseDto<T> {
  private List<T> items;
  private Long nextCursor;
}
