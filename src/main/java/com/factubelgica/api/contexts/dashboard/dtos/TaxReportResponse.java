package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.Taxes;

import java.math.BigDecimal;
import java.util.List;

public record TaxReportResponse(
    int year,
    int month,
    BigDecimal totalTaxesYear,
    BigDecimal totalTaxesMonth,
    List<MonthlyTaxMetric> yearlyTaxes,
    List<DailyTaxMetric> monthlyTaxes
) {
  public static TaxReportResponse fromDomain(int year, int month, Taxes domain) {
    return new TaxReportResponse(
        year,
        month,
        domain.getTotalTaxesYear(),
        domain.getTotalTaxesMonth(),
        domain.getYearlyTaxes().stream().map(MonthlyTaxMetric::fromDomain).toList(),
        domain.getMonthlyTaxes().stream().map(DailyTaxMetric::fromDomain).toList()
    );
  }
}
