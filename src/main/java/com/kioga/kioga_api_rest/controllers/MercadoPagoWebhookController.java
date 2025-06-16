package com.kioga.kioga_api_rest.controllers;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kioga.kioga_api_rest.services.MercadoPagoService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mercadopago/webhook")
@RequiredArgsConstructor
public class MercadoPagoWebhookController {
  private final MercadoPagoService mercadoPagoService;

  @PostMapping
  public ResponseEntity<?> handleWebhook(HttpServletRequest request,
      @RequestParam("data.id") String dataIdParam) throws IOException {
    String signature = request.getHeader("x-signature");
    String payload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    String requestId = request.getHeader("X-Request-Id");
    mercadoPagoService.handleWebhook(signature, payload, requestId, dataIdParam);
    return ResponseEntity.ok().build();
  }
}
