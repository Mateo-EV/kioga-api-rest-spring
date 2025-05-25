package com.kioga.kioga_api_rest.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.kioga.kioga_api_rest.services.MailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

  private final JavaMailSender mailSender;

  @Value("${base-url}")
  private String baseUrl;

  public void sendVerificationEmail(String to, String token) {
    String link = baseUrl + "/api/auth/verify?token=" + token;

    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(to);
    msg.setSubject("Verifica tu cuenta");
    msg.setText("Haz clic aquí para verificar tu cuenta: " + link);
    mailSender.send(msg);
  }

  public void sendPasswordResetEmail(String to, String token) {
    String link = baseUrl + "/api/auth/reset-password?token=" + token;

    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(to);
    msg.setSubject("Restablecer contraseña");
    msg.setText("Haz clic aquí para restablecer tu contraseña: " + link);
    mailSender.send(msg);
  }
}
