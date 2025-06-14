package com.kioga.kioga_api_rest.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kioga.kioga_api_rest.dto.order.OrderDto;
import com.kioga.kioga_api_rest.dto.order.StartOrderRequestDto;
import com.kioga.kioga_api_rest.entities.Order;
import com.kioga.kioga_api_rest.entities.Product;
import com.kioga.kioga_api_rest.entities.User;
import com.kioga.kioga_api_rest.mappers.OrderMapper;
import com.kioga.kioga_api_rest.repositories.OrderRepository;
import com.kioga.kioga_api_rest.repositories.ProductRepository;
import com.kioga.kioga_api_rest.services.MercadoPagoService;
import com.kioga.kioga_api_rest.services.OrderService;
import com.kioga.kioga_api_rest.services.impl.MercadoPagoServiceImpl.ProductDetailItem;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  private final MercadoPagoService mercadoPagoService;

  private final OrderMapper orderMapper;

  @Override
  public List<OrderDto> getOrdersByUser(User user) {
    return orderMapper.toDto(orderRepository.findByUser(user));
  }

  @Override
  @Transactional
  public OrderDto startOrder(StartOrderRequestDto request, User user) {
    Order order = orderMapper.toEntity(request);

    List<Long> productIds = order.getOrderProducts().stream()
        .map(detail -> detail.getProduct().getId())
        .distinct()
        .toList();

    List<Product> products = productRepository.findAllById(productIds);
    Map<Long, Product> productMap = products.stream()
        .collect(Collectors.toMap(Product::getId, p -> p));

    List<ProductDetailItem> productDetails = order.getOrderProducts().stream()
        .map(detail -> {
          Product product = productMap.get(detail.getProduct().getId());
          if (product == null) {
            throw new EntityNotFoundException("Product not found: " + detail.getProduct().getId());
          }
          return ProductDetailItem.builder()
              .id(product.getId().toString())
              .title(product.getName())
              .quantity(detail.getQuantity())
              .unitPrice(product.getPrice())
              .build();
        })
        .toList();

    mercadoPagoService.createPaymentPreference(
        productDetails,
        order.getIsDelivery(),
        order.getUser().getEmail());

    return orderMapper.toDto(order);
  }
}
