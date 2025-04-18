package com.kioga.kioga_api_rest.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id
  @Column(name = "order_id")
  private Long orderId;

  @OneToOne
  // @MapsId
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "dni", nullable = false, length = 10)
  private String dni;

  @Column(name = "phone", nullable = false, length = 20)
  private String phone;

  @Column(name = "department")
  private String department;

  @Column(name = "province")
  private String province;

  @Column(name = "district")
  private String district;

  @Column(name = "street_address")
  private String streetAddress;

  @Column(name = "zip_code")
  private String zipCode;

  @Column(name = "reference", columnDefinition = "TEXT")
  private String reference;
}
