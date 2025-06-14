package com.kioga.kioga_api_rest.services.impl;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kioga.kioga_api_rest.entities.Address;
import com.kioga.kioga_api_rest.entities.Order;
import com.kioga.kioga_api_rest.entities.OrderProduct;
import com.kioga.kioga_api_rest.entities.Product;
import com.kioga.kioga_api_rest.entities.User;
import com.kioga.kioga_api_rest.entities.embeds.OrderProductId;
import com.kioga.kioga_api_rest.entities.enums.OrderStatus;
import com.kioga.kioga_api_rest.repositories.OrderRepository;
import com.kioga.kioga_api_rest.repositories.ProductRepository;
import com.kioga.kioga_api_rest.repositories.UserRepository;
import com.kioga.kioga_api_rest.services.MercadoPagoService;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MercadoPagoServiceImpl implements MercadoPagoService {

  @Value("${frontend.base-url}")
  private String frontendBaseUrl;

  @Value("${base-url}")
  private String baseUrl;

  @Value("${mercadopago.webhook-token}")
  private String webhookSecret;

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static final class ProductDetailItem {
    private String id;
    private String title;
    private Integer quantity;
    private BigDecimal unitPrice;
  }

  @Override
  public Preference createPaymentPreference(
      List<ProductDetailItem> products,
      Boolean isDelivery,
      String payerEmail) {
    try {

      List<PreferenceItemRequest> items = new ArrayList<>();
      for (ProductDetailItem p : products) {
        items.add(PreferenceItemRequest.builder()
            .id(p.getId().toString())
            .title(p.getTitle())
            .quantity(p.getQuantity())
            .unitPrice(p.getUnitPrice())
            .currencyId("PEN")
            .build());
      }

      if (isDelivery) {
        items.add(PreferenceItemRequest.builder()
            .id("delivery_fee")
            .title("Delivery Fee")
            .quantity(1)
            .unitPrice(new BigDecimal("5.00"))
            .currencyId("PEN")
            .build());
      }

      PreferenceRequest prefReq = PreferenceRequest.builder()
          .items(items)
          .payer(PreferencePayerRequest.builder().email(payerEmail).build())
          .backUrls(PreferenceBackUrlsRequest.builder()
              .success(frontendBaseUrl + "/pedidos?success=true")
              .failure(frontendBaseUrl + "/pedidos?error=false")
              .build())
          .notificationUrl(baseUrl + "/api/mercadopago/webhook")
          .autoReturn("approved")
          .build();

      Preference preference = new PreferenceClient().create(prefReq);

      return preference;
    } catch (MPException e) {
      throw new RuntimeException("Error creating Mercado Pago preference", e);
    } catch (MPApiException e) {
      throw new RuntimeException("Error with Mercado Pago API", e);
    }
  }

  @Override
  public void handleWebhook(String signature, String payload) {
    if (!isValidSignature(signature, payload, webhookSecret)) {
      throw new SecurityException("Invalid signature");
    }

    try {

      ObjectMapper mapper = new ObjectMapper();
      JsonNode body = mapper.readTree(payload);
      String type = body.path("type").asText();
      String action = body.path("action").asText();
      Long dataId = body.path("data").path("id").asLong();

      if (!"payment".equals(type) || !"payment.created".equals(action)) {
        return;
      }

      Optional<Payment> paymentOpt = getPaymentDetails(dataId);
      if (paymentOpt.isEmpty()) {
        throw new EntityNotFoundException("Payment not found: " + dataId);
      }

      createOrderFromPayment(paymentOpt.get());
    } catch (Exception e) {
      throw new RuntimeException("Error processing Mercado Pago webhook", e);
    }
  }

  private boolean isValidSignature(String signature, String payload, String secret) {
    if (signature == null || secret == null)
      return false;

    try {
      Mac sha256Hmac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      sha256Hmac.init(secretKey);
      byte[] hash = sha256Hmac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
      String expected = HexFormat.of().formatHex(hash);
      return MessageDigest.isEqual(expected.getBytes(), signature.getBytes());
    } catch (Exception e) {
      return false;
    }
  }

  private Optional<Payment> getPaymentDetails(Long paymentId) {
    try {
      PaymentClient client = new PaymentClient();
      return Optional.of(client.get(paymentId));
    } catch (MPApiException e) {
      return Optional.empty();
    } catch (MPException e) {
      return Optional.empty();
    }
  }

  public void createOrderFromPayment(Payment payment) {
    Map<String, Object> metadata = payment.getMetadata();

    Order order = new Order();
    User user = userRepository.findById((Long) metadata.get("user_id"))
        .orElseThrow(() -> new EntityNotFoundException("User not found"));
    order.setUser(user);

    if (metadata.containsKey("address")) {
      Map<String, Object> addressData = (Map<String, Object>) metadata.get("address");
      Address address = Address.builder()
          .firstName((String) addressData.get("first_name"))
          .lastName((String) addressData.get("last_name"))
          .dni((String) addressData.get("dni"))
          .phone((String) addressData.get("phone"))
          .department((String) addressData.get("department"))
          .province((String) addressData.get("province"))
          .district((String) addressData.get("district"))
          .streetAddress((String) addressData.get("street_address"))
          .zipCode((String) addressData.get("zip_code"))
          .reference((String) addressData.get("reference"))
          .build();
      order.setAddress(address);
    }

    List<Map<String, Object>> orderProducts = (List<Map<String, Object>>) metadata.get("details");
    List<OrderProduct> orderProductList = new ArrayList<>();

    List<Long> productIds = orderProducts.stream()
        .map(detail -> (Long) detail.get("product_id"))
        .distinct()
        .toList();
    List<Product> products = productRepository.findAllById(productIds);
    Map<Long, Product> productMap = products.stream()
        .collect(Collectors.toMap(Product::getId, p -> p));

    for (Map<String, Object> detail : orderProducts) {
      Long productId = (Long) detail.get("product_id");
      Product product = productMap.get(productId);
      if (product == null) {
        throw new EntityNotFoundException("Product not found: " + productId);
      }

      OrderProduct orderProduct = new OrderProduct();
      orderProduct.setProduct(product);
      orderProduct.setQuantity((Integer) detail.get("quantity"));
      orderProduct.setOrder(order);
      orderProduct.setUnitAmount(product.getPrice());
      orderProduct.setId(OrderProductId.builder().productId(productId).orderId(order.getId()).build());

      orderProductList.add(orderProduct);
    }

    order.setOrderProducts(orderProductList);
    order.setAmount(orderProductList.stream()
        .map(op -> op.getUnitAmount().multiply(BigDecimal.valueOf(op.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add));
    order.setStatus(OrderStatus.Pending);
    order.setIsDelivery((Boolean) metadata.get("is_delivery"));
    order.setShippingAmount(order.getIsDelivery() ? new BigDecimal("5.00") : BigDecimal.ZERO);
    order.setNotes((String) metadata.get("notes"));

    orderRepository.save(order);
  }
}
