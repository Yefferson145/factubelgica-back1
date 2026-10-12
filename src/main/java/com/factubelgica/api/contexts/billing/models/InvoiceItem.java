package com.factubelgica.api.contexts.billing.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class InvoiceItem {
  private final Integer itemId;
  private final Integer productId;
  private final BigDecimal quantity;
  private final String description;
  private final BigDecimal netPrice;
}
