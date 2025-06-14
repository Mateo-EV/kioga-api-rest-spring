package com.kioga.kioga_api_rest.dto.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.kioga.kioga_api_rest.dto.product.ProductDto;
import com.kioga.kioga_api_rest.entities.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UserOrderDto {
    private Long id;
    private String name;
    private String email;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AddressOrderDto {
    private String firstName;
    private String lastName;
    private String dni;
    private String phone;
    private String department;
    private String province;
    private String district;
    private String streetAddress;
    private String zipCode;
    private String reference;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class DetailOrderDto {
    private Integer quantity;
    private BigDecimal unitAmount;
    private ProductDto product;
  }

  private Long id;
  private BigDecimal amount;
  private UserOrderDto user;
  private OrderStatus status;
  private BigDecimal shippingAmount;
  private Boolean isDelivery;
  private String notes;
  private AddressOrderDto address;
  private Instant createdAt;
  private List<DetailOrderDto> orderProducts;
}
