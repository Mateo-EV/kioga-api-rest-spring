package com.kioga.kioga_api_rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kioga.kioga_api_rest.services.MercadoPagoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mercadopago/webhook")
@RequiredArgsConstructor
public class MercadoPagoWebhookController {
  private final MercadoPagoService mercadoPagoService;

  @PostMapping
  public ResponseEntity<?> handleWebhook(
      @RequestHeader(value = "x-signature", required = false) String signature,
      @RequestBody String payload) {
    mercadoPagoService.handleWebhook(signature, payload);
    return ResponseEntity.ok().build();
  }
}
