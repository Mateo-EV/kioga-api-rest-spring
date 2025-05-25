package com.kioga.kioga_api_rest.dto.auth.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequestDto {
  @NotBlank
  private String token;

  @NotBlank
  @Min(value = 8, message = "Password must be at least 8 characters long")
  private String newPassword;

  @NotBlank(message = "Email is required")
  private String email;
}
