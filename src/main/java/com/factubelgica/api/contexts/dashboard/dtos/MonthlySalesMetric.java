package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.MonthlySales;
import java.math.BigDecimal;

public record MonthlySalesMetric(
    int month,
    BigDecimal netSales,
    BigDecimal grossSales
) {
  public static MonthlySalesMetric fromDomain(MonthlySales domain) {
    return new MonthlySalesMetric(domain.getMonth(), domain.getNetSales(), domain.getGrossSales());
  }
}
