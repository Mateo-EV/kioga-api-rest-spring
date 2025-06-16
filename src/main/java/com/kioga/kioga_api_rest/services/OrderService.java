package com.kioga.kioga_api_rest.services;

import java.util.List;

import com.kioga.kioga_api_rest.dto.order.OrderDto;
import com.kioga.kioga_api_rest.dto.order.StartOrderRequestDto;
import com.kioga.kioga_api_rest.entities.User;
import com.mercadopago.resources.preference.Preference;

public interface OrderService {
  List<OrderDto> getOrdersByUser(User user);

  Preference startOrder(StartOrderRequestDto request, User user);

  // void updateOrderStatus();

  // void getOrderById();

  // void getOrdersPaginatedByStatus();

  // void deleteOrderById();
}
