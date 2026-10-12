package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.MonthlyProductSales;
import java.math.BigDecimal;

public record MonthlyProductSalesMetric(
    int month,
    BigDecimal quantity,
    BigDecimal netSales
) {
  public static MonthlyProductSalesMetric fromDomain(MonthlyProductSales domain) {
    return new MonthlyProductSalesMetric(domain.getMonth(), domain.getQuantity(), domain.getNetSales());
  }
}
