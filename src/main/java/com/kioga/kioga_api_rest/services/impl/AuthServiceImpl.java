package com.kioga.kioga_api_rest.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kioga.kioga_api_rest.dto.auth.request.ForgotPasswordRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.LoginRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.RegisterRequestDto;
import com.kioga.kioga_api_rest.dto.auth.request.ResetPasswordRequestDto;
import com.kioga.kioga_api_rest.dto.auth.response.AuthResponseDto;
import com.kioga.kioga_api_rest.entities.PasswordResetToken;
import com.kioga.kioga_api_rest.entities.User;
import com.kioga.kioga_api_rest.entities.VerificationToken;
import com.kioga.kioga_api_rest.exceptions.BadRequestException;
import com.kioga.kioga_api_rest.repositories.PasswordResetTokenRepository;
import com.kioga.kioga_api_rest.repositories.UserRepository;
import com.kioga.kioga_api_rest.repositories.VerificationTokenRepository;
import com.kioga.kioga_api_rest.security.jwt.JwtTokenProvider;
import com.kioga.kioga_api_rest.services.AuthService;
import com.kioga.kioga_api_rest.services.MailService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PasswordEncoder encoder;

  private final VerificationTokenRepository tokenRepository;
  private final UserRepository userRepository;
  private final PasswordResetTokenRepository passwordResetTokenRepository;

  private final MailService mailService;

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
    boolean userExists = userRepository.existsByEmail(registerRequestDto.getEmail());

    if (userExists) {
      throw new RuntimeException("Este email ya está en uso");
    }

    User user = User.builder()
        .name(registerRequestDto.getName())
        .email(registerRequestDto.getEmail())
        .hashedPassword(encoder.encode(registerRequestDto.getPassword()))
        .isEmailValid(false)
        .build();

    user = userRepository.save(user);

    String token = UUID.randomUUID().toString();
    VerificationToken vt = VerificationToken.builder()
        .token(token)
        .user(user)
        .expiryDate(LocalDateTime.now().plusHours(24))
        .build();
    tokenRepository.save(vt);

    mailService.sendVerificationEmail(user.getEmail(), token);

    String tokenJwt = jwtTokenProvider.generateToken(String.valueOf(user.getId()));

    return new AuthResponseDto(tokenJwt);
  }

  @Override
  @Transactional
  public void verify(String token) {
    VerificationToken vt = tokenRepository.findByToken(token)
        .orElseThrow(() -> new RuntimeException("Token no encontrado"));

    User user = vt.getUser();
    user.setIsEmailValid(true);
    userRepository.save(user);
    tokenRepository.delete(vt);
  }

  @Override
  public AuthResponseDto login(LoginRequestDto loginRequestDto) {
    User user = userRepository.findByEmail(loginRequestDto.getEmail())
        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

    if (!encoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
      throw new BadRequestException("Contraseña incorrecta");
    }

    String token = jwtTokenProvider.generateToken(String.valueOf(user.getId()));

    return new AuthResponseDto(token);
  }

  @Override
  public AuthResponseDto sendPasswordResetToken(ForgotPasswordRequestDto forgotPasswordRequest) {
    User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
        .orElseThrow(() -> new EntityNotFoundException("Email no registrado"));

    String token = UUID.randomUUID().toString();
    PasswordResetToken resetToken = PasswordResetToken.builder()
        .token(token)
        .user(user)
        .expiryDate(LocalDateTime.now().plusHours(1))
        .build();
    passwordResetTokenRepository.save(resetToken);

    mailService.sendPasswordResetEmail(user.getEmail(), token);

    return new AuthResponseDto("Se ha enviado un correo electrónico para restablecer la contraseña.");
  }

  @Override
  public AuthResponseDto resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
    User user = userRepository.findByEmail(resetPasswordRequestDto.getEmail())
        .orElseThrow(() -> new EntityNotFoundException("Email no registrado"));

    PasswordResetToken resetToken = passwordResetTokenRepository
        .findByTokenAndUser(resetPasswordRequestDto.getToken(), user)
        .orElseThrow(() -> new EntityNotFoundException("Token no encontrado"));

    if (resetToken.isExpired()) {
      throw new BadRequestException("Token inválido o expirado");
    }

    user.setHashedPassword(encoder.encode(resetPasswordRequestDto.getNewPassword()));
    userRepository.save(user);

    return new AuthResponseDto("Contraseña restablecida exitosamente.");
  }

  @Override
  public AuthResponseDto resendVerification(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

    tokenRepository.delete(tokenRepository.findByUser(user)
        .orElseThrow(() -> new EntityNotFoundException("Token no encontrado")));

    if (user.getIsEmailValid()) {
      throw new BadRequestException("El usuario ya está verificado.");
    }

    String token = UUID.randomUUID().toString();
    VerificationToken vt = VerificationToken.builder()
        .token(token)
        .user(user)
        .expiryDate(LocalDateTime.now().plusHours(24))
        .build();
    tokenRepository.save(vt);

    mailService.sendVerificationEmail(user.getEmail(), token);

    return new AuthResponseDto("Se ha reenviado el correo de verificación.");
  }

}
