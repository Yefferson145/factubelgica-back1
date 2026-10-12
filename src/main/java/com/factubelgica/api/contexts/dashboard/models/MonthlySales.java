package com.factubelgica.api.contexts.dashboard.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class MonthlySales {
  private final int month;
  private final BigDecimal netSales;
  private final BigDecimal grossSales;
}
