package com.kioga.kioga_api_rest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kioga.kioga_api_rest.entities.User;
import com.kioga.kioga_api_rest.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
  Optional<VerificationToken> findByToken(String token);

  Optional<VerificationToken> findByUser(User user);

  void deleteByUser(User user);
}
