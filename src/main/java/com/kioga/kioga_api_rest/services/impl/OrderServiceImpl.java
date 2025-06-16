package com.kioga.kioga_api_rest.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
import com.mercadopago.resources.preference.Preference;

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
  public Preference startOrder(StartOrderRequestDto request, User user) {
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

    Map<String, Object> addressMap = new HashMap<>();
    addressMap.put("first_name", order.getAddress().getFirstName());
    addressMap.put("last_name", order.getAddress().getLastName());
    addressMap.put("phone", order.getAddress().getPhone());
    addressMap.put("dni", order.getAddress().getDni());
    addressMap.put("reference", order.getAddress().getReference());
    addressMap.put("department", order.getAddress().getDepartment());
    addressMap.put("province", order.getAddress().getProvince());
    addressMap.put("district", order.getAddress().getDistrict());
    addressMap.put("street_address", order.getAddress().getStreetAddress());
    addressMap.put("zip_code", order.getAddress().getZipCode());

    List<Map<String, Object>> detailsList = productDetails.stream()
        .map(detail -> {
          Map<String, Object> detailMap = new HashMap<>();
          detailMap.put("product_id", detail.getId());
          detailMap.put("quantity", detail.getQuantity());
          return detailMap;
        }).collect(Collectors.toList());

    Map<String, Object> finalMap = new HashMap<>();
    finalMap.put("user_id", user.getId());
    finalMap.put("is_delivery", order.getIsDelivery());
    finalMap.put("address", addressMap);
    finalMap.put("details", detailsList);
    finalMap.put("notes", order.getNotes());

    return mercadoPagoService.createPaymentPreference(
        productDetails,
        order.getIsDelivery(),
        user.getEmail(),
        finalMap);
  }
}
