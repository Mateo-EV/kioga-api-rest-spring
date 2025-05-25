package com.kioga.kioga_api_rest.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResendVerificationRequestDto {
  @NotBlank(message = "Email is required")
  private String email;
}
