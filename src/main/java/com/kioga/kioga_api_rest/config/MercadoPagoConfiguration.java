package com.kioga.kioga_api_rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.mercadopago.MercadoPagoConfig;

import jakarta.annotation.PostConstruct;

@Configuration
public class MercadoPagoConfiguration {
  @Value("${mercadopago.access-token}")
  private String accessToken;

  @PostConstruct
  void mercadoPagoConfig() {
    MercadoPagoConfig.setAccessToken(accessToken);
  }
}
