package com.kioga.kioga_api_rest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kioga.kioga_api_rest.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  // Optional<User> findByVerificationToken(VerificationToken verificationToken);

  // Optional<User> findByResetPasswordToken(ResetPasswordToken
  // resetPasswordToken);

}
