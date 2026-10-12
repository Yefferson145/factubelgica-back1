package com.factubelgica.api.contexts.catalog.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class Product {
  @NonNull
  private final Integer productId;
  @NonNull
  private final String description;
  @NonNull
  private final BigDecimal netPrice;
  @NonNull
  private final Boolean active;
}