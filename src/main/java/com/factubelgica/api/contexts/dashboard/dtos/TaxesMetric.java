package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.Taxes;
import java.math.BigDecimal;
import java.util.List;

public record TaxesMetric(
    BigDecimal totalTaxesYear,
    BigDecimal totalTaxesMonth,
    List<MonthlyTaxMetric> yearlyTaxes,
    List<DailyTaxMetric> monthlyTaxes
) {
  public static TaxesMetric fromDomain(Taxes domain) {
    return new TaxesMetric(
        domain.getTotalTaxesYear(),
        domain.getTotalTaxesMonth(),
        domain.getYearlyTaxes().stream().map(MonthlyTaxMetric::fromDomain).toList(),
        domain.getMonthlyTaxes().stream().map(DailyTaxMetric::fromDomain).toList()
    );
  }
}
