package com.kioga.kioga_api_rest.controllers;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
import com.kioga.kioga_api_rest.dto.auth.request.ResetPasswordRequestDto;
import com.kioga.kioga_api_rest.dto.auth.response.AuthResponseDto;
import com.kioga.kioga_api_rest.dto.auth.response.UserProfileResponseDto;
import com.kioga.kioga_api_rest.entities.User;
import com.kioga.kioga_api_rest.services.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @Value("${frontend.base-url}")
  private String frontendUrl;

  private String temporalDomain = ".railway.app";

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto request,
      HttpServletResponse response) {
    AuthResponseDto authResponse = authService.register(request);

    ResponseCookie cookie = ResponseCookie.from("kioga_token", authResponse.getMessage())
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(Duration.ofDays(1))
        .sameSite("None")
        .domain(temporalDomain)
        .build();

    response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(authResponse);
  }

  @GetMapping("/verify")
  public ResponseEntity<Void> verify(@RequestParam String token, HttpServletResponse response) throws IOException {
    authService.verify(token);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(frontendUrl));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @GetMapping("/profile")
  public ResponseEntity<UserProfileResponseDto> getProfile(@AuthenticationPrincipal User userDetails) {
    return ResponseEntity
        .ok(new UserProfileResponseDto(userDetails.getName(), userDetails.getEmail(), userDetails.getIsEmailValid()));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto request,
      HttpServletResponse response) {
    AuthResponseDto authResponse = authService.login(request);

    ResponseCookie cookie = ResponseCookie.from("kioga_token", authResponse.getMessage())
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(Duration.ofDays(1))
        .sameSite("None")
        .domain(temporalDomain)
        .build();

    response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    return ResponseEntity.ok(authResponse);
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
  public ResponseEntity<AuthResponseDto> resendVerification(@AuthenticationPrincipal User userDetails) {
    return ResponseEntity.ok(authService.resendVerification(userDetails.getEmail()));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    ResponseCookie deleteCookie = ResponseCookie.from("kioga_token", "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .sameSite("None")
        .domain(temporalDomain)
        .build();

    response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
    return ResponseEntity.noContent().build();
  }

}
