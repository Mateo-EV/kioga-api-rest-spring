package com.kioga.kioga_api_rest.dto.order.validation.impl;

import com.kioga.kioga_api_rest.dto.order.StartOrderRequestDto;
import com.kioga.kioga_api_rest.dto.order.validation.AddressRequiredIfDelivery;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AddressRequiredIfDeliveryImpl
    implements ConstraintValidator<AddressRequiredIfDelivery, StartOrderRequestDto> {

  @Override
  public boolean isValid(StartOrderRequestDto dto, ConstraintValidatorContext context) {
    if (dto == null)
      return true;

    if (Boolean.TRUE.equals(dto.getIsDelivery())) {
      if (dto.getAddress() == null ||
          dto.getAddress().getFirstName().isEmpty() ||
          dto.getAddress().getLastName().isEmpty() ||
          dto.getAddress().getDepartment().isEmpty() ||
          dto.getAddress().getDistrict().isEmpty() ||
          dto.getAddress().getPhone().isEmpty() ||
          dto.getAddress().getProvince().isEmpty() ||
          dto.getAddress().getStreetAddress().isEmpty()) {
        return false;
      }
    }

    return true;
  }
}
