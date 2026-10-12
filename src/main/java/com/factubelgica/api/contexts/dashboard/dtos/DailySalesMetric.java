package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.DailySales;
import java.math.BigDecimal;

public record DailySalesMetric(
    int day,
    BigDecimal netSales,
    BigDecimal grossSales
) {
  public static DailySalesMetric fromDomain(DailySales domain) {
    return new DailySalesMetric(domain.getDay(), domain.getNetSales(), domain.getGrossSales());
  }
}
