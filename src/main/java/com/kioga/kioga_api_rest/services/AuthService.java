package com.kioga.kioga_api_rest.services;

import com.kioga.kioga_api_rest.dto.auth.request.ForgotPasswordRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.LoginRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.RegisterRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.ResendVerificationRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.ResetPasswordRequestDto;
import com.kioga.kioga_api_rest.dto.auth.response.AuthResponseDto;

public interface AuthService {
  AuthResponseDto register(RegisterRequestDto registerRequestDto);

  void verify(String token);

  AuthResponseDto login(LoginRequestDto loginRequestDto);

  AuthResponseDto sendPasswordResetToken(ForgotPasswordRequestDto forgotPasswordRequest);

  AuthResponseDto resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);

  AuthResponseDto resendVerification(ResendVerificationRequestDto resendVerificationRequestDto);
}
