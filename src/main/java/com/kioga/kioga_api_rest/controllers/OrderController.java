package com.kioga.kioga_api_rest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kioga.kioga_api_rest.dto.order.OrderDto;
import com.kioga.kioga_api_rest.dto.order.StartOrderRequestDto;
import com.kioga.kioga_api_rest.entities.User;
import com.kioga.kioga_api_rest.services.OrderService;
import com.mercadopago.resources.preference.Preference;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @PostMapping("/start")
  public Preference startOrder(@RequestBody @Valid StartOrderRequestDto request, @AuthenticationPrincipal User user) {
    return orderService.startOrder(request, user);
  }

  @GetMapping("/personal")
  public ResponseEntity<List<OrderDto>> getPersonalOrders(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(orderService.getOrdersByUser(user));
  }

}
