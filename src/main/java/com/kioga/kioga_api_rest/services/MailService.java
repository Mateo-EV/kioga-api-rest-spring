package com.kioga.kioga_api_rest.services;

public interface MailService {
  void sendVerificationEmail(String to, String token);

  void sendPasswordResetEmail(String to, String token);
}
