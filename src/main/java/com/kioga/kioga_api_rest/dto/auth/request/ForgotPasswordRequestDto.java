package com.kioga.kioga_api_rest.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequestDto {
  @NotBlank(message = "Email is required")
  private String email;
}
