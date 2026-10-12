package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.SalesReport;

import java.math.BigDecimal;
import java.util.List;

public record SalesReportResponse(
    int year,
    int month,
    BigDecimal totalNetSalesYear,
    BigDecimal totalGrossSalesYear,
    BigDecimal totalNetSalesMonth,
    BigDecimal totalGrossSalesMonth,
    List<MonthlySalesMetric> yearlySales,
    List<DailySalesMetric> monthlySales
) {
  public static SalesReportResponse fromDomain(SalesReport domain) {
    return new SalesReportResponse(
        domain.getYear(),
        domain.getMonth(),
        domain.getTotalNetSalesYear(),
        domain.getTotalGrossSalesYear(),
        domain.getTotalNetSalesMonth(),
        domain.getTotalGrossSalesMonth(),
        domain.getYearlySales().stream().map(MonthlySalesMetric::fromDomain).toList(),
        domain.getMonthlySales().stream().map(DailySalesMetric::fromDomain).toList()
    );
  }
}
