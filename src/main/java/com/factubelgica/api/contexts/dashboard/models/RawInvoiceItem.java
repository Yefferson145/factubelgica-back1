package com.factubelgica.api.contexts.dashboard.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class RawInvoiceItem {
  private final Integer productId;
  private final String productDescription;
  private final BigDecimal productNetPrice;
  private final LocalDate issueDate;
  private final BigDecimal quantity;
  private final BigDecimal itemNetPrice;
  private final BigDecimal vatRate;
}
