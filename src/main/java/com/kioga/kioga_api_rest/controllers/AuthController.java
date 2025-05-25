package com.kioga.kioga_api_rest.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kioga.kioga_api_rest.dto.auth.request.ForgotPasswordRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.LoginRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.RegisterRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.ResendVerificationRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.ResetPasswordRequestDto;
import com.kioga.kioga_api_rest.dto.auth.response.AuthResponseDto;
import com.kioga.kioga_api_rest.dto.auth.response.UserProfileResponseDto;
import com.kioga.kioga_api_rest.entities.User;
import com.kioga.kioga_api_rest.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @Value("${frontend.base-url}")
  private String frontendUrl;

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @GetMapping("/verify")
  public String verify(@RequestParam String token) {
    authService.verify(token);

    return "redirect:" + frontendUrl;
  }

  @GetMapping("/profile")
  public ResponseEntity<UserProfileResponseDto> getProfile(@AuthenticationPrincipal User userDetails) {
    return ResponseEntity
        .ok(new UserProfileResponseDto(userDetails.getName(), userDetails.getEmail(), userDetails.getIsEmailValid()));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<AuthResponseDto> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDto request) {
    return ResponseEntity.ok(authService.sendPasswordResetToken(request));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<AuthResponseDto> resetPassword(@RequestBody @Valid ResetPasswordRequestDto request) {
    return ResponseEntity.ok(authService.resetPassword(request));
  }

  @PostMapping("/resend-verification")
  public ResponseEntity<AuthResponseDto> resendVerification(@RequestBody @Valid ResendVerificationRequestDto request) {
    return ResponseEntity.ok(authService.resendVerification(request));
  }
}
