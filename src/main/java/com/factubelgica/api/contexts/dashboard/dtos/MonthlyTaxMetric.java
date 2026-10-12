package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.MonthlyTax;
import java.math.BigDecimal;

public record MonthlyTaxMetric(
    int month,
    BigDecimal taxAmount
) {
  public static MonthlyTaxMetric fromDomain(MonthlyTax domain) {
    return new MonthlyTaxMetric(domain.getMonth(), domain.getTaxAmount());
  }
}
