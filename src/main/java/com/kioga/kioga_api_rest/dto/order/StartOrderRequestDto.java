package com.kioga.kioga_api_rest.dto.order;

import java.util.List;

import com.kioga.kioga_api_rest.dto.order.validation.AddressRequiredIfDelivery;
import com.kioga.kioga_api_rest.validation.UniqueField;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AddressRequiredIfDelivery
public class StartOrderRequestDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static final class AddressStartOrderRequestDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    @NotBlank(message = "El departamento es obligatorio")
    private String department;

    @NotBlank(message = "La provincia es obligatoria")
    private String province;

    @NotBlank(message = "El distrito es obligatorio")
    private String district;

    @NotBlank(message = "La dirección de la calle es obligatoria")
    private String streetAddress;

    private String zipCode;
    private String reference;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static final class ProductStartOrderRequestDto {
    @NotNull(message = "El ID del producto no puede ser nulo")
    private Long productId;

    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser un número positivo.")
    @Min(value = 1, message = "La cantidad mínima debe ser 1.")
    private Integer quantity;
  }

  @NotNull(message = "El estado de entrega no puede ser nulo")
  private Boolean isDelivery;

  @NotNull(message = "La dirección no puede ser nula")
  private AddressStartOrderRequestDto address;

  private String notes;

  @NotNull(message = "La lista de productos no puede ser nula")
  @Size(min = 1, message = "Debe haber al menos un detalle en la orden.")
  @UniqueField(fieldName = "productId")
  private List<@Valid ProductStartOrderRequestDto> orderProducts;
}
