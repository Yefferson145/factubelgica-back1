package com.factubelgica.api.contexts.dashboard.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class DailySales {
  private final int day;
  private final BigDecimal netSales;
  private final BigDecimal grossSales;
}
