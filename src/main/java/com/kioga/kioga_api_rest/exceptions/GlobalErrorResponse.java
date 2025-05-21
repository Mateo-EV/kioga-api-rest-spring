package com.kioga.kioga_api_rest.exceptions;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalErrorResponse {
  private Instant timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
}
