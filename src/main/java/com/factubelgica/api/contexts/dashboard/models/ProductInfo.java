package com.factubelgica.api.contexts.dashboard.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class ProductInfo {
  private final int productId;
  private final String description;
  private final BigDecimal netPrice;
}
