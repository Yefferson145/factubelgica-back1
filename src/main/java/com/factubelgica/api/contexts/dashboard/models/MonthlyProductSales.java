package com.factubelgica.api.contexts.dashboard.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class MonthlyProductSales {
  private final int month;
  private final BigDecimal quantity;
  private final BigDecimal netSales;
}
