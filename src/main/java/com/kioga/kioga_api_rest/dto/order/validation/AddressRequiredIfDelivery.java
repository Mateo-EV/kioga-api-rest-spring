package com.kioga.kioga_api_rest.dto.order.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import com.kioga.kioga_api_rest.dto.order.validation.impl.AddressRequiredIfDeliveryImpl;

@Documented
@Constraint(validatedBy = AddressRequiredIfDeliveryImpl.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressRequiredIfDelivery {
  String message() default "La dirección es obligatoria si el pedido es con entrega (delivery)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
