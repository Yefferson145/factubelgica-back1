package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.DailyTax;
import java.math.BigDecimal;

public record DailyTaxMetric(
    int day,
    BigDecimal taxAmount
) {
  public static DailyTaxMetric fromDomain(DailyTax domain) {
    return new DailyTaxMetric(domain.getDay(), domain.getTaxAmount());
  }
}
