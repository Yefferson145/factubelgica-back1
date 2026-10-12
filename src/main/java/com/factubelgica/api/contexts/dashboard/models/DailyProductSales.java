package com.factubelgica.api.contexts.dashboard.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class DailyProductSales {
  private final int day;
  private final BigDecimal quantity;
  private final BigDecimal netSales;
}
