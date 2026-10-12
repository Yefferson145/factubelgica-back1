package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.DailyProductSales;
import java.math.BigDecimal;

public record DailyProductSalesMetric(
    int day,
    BigDecimal quantity,
    BigDecimal netSales
) {
  public static DailyProductSalesMetric fromDomain(DailyProductSales domain) {
    return new DailyProductSalesMetric(domain.getDay(), domain.getQuantity(), domain.getNetSales());
  }
}
