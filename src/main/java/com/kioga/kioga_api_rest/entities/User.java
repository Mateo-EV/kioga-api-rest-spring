package com.kioga.kioga_api_rest.entities;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, name = "is_email_valid")
  @ColumnDefault("false")
  private Boolean isEmailValid;

  @Column(name = "password", nullable = false)
  private String hashedPassword;

  @Column(name = "created_at")
  @CreationTimestamp
  private Instant createdAt;

  @OneToMany(mappedBy = "user")
  private List<Order> orders;

  // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  // private PasswordResetToken passwordResetToken;

  // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  // private VerificationToken verificationToken;

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return hashedPassword;
  }
}