package com.kioga.kioga_api_rest.services;

import java.util.List;
import java.util.Map;

import com.kioga.kioga_api_rest.services.impl.MercadoPagoServiceImpl.ProductDetailItem;
import com.mercadopago.resources.preference.Preference;

public interface MercadoPagoService {
  Preference createPaymentPreference(
      List<ProductDetailItem> products,
      Boolean isDelivery,
      String payerEmail,
      Map<String, Object> metadata);

  void handleWebhook(String signature, String payload, String requestId, String dataId);
}
