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
      if (dto.getAddress() == null) {
        return false;
      }
    }

    return true;
  }
}
