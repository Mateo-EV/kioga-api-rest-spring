package com.kioga.kioga_api_rest.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeRequestDto {
  private String oldPassword;
  private String newPassword;
}
