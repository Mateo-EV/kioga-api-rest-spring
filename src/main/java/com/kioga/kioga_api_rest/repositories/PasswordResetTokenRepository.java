package com.kioga.kioga_api_rest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kioga.kioga_api_rest.entities.PasswordResetToken;
import com.kioga.kioga_api_rest.entities.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
  Optional<PasswordResetToken> findByTokenAndUser(String token, User user);
}
