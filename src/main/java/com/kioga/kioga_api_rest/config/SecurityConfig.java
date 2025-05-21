package com.kioga.kioga_api_rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {
    http
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/**")
                .permitAll()
                .anyRequest()
                .authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint((request, response, authException) -> {
              response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }))
        .formLogin(form -> form.disable())
        .logout(logout -> logout.disable());

    return http.build();
  }
}
