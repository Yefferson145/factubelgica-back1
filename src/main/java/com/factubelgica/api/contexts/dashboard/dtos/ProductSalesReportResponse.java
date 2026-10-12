package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.ProductSales;

import java.math.BigDecimal;
import java.util.List;

public record ProductSalesReportResponse(
    int year,
    int month,
    int productId,
    String description,
    BigDecimal currentNetPrice,
    BigDecimal totalQuantityYear,
    BigDecimal totalNetSalesYear,
    BigDecimal totalQuantityMonth,
    BigDecimal totalNetSalesMonth,
    List<MonthlyProductSalesMetric> monthlySales,
    List<DailyProductSalesMetric> dailySales
) {
  public static ProductSalesReportResponse fromDomain(int year, int month, ProductSales domain) {
    return new ProductSalesReportResponse(
        year,
        month,
        domain.getProductId(),
        domain.getDescription(),
        domain.getCurrentNetPrice(),
        domain.getTotalQuantityYear(),
        domain.getTotalNetSalesYear(),
        domain.getTotalQuantityMonth(),
        domain.getTotalNetSalesMonth(),
        domain.getMonthlySales().stream().map(MonthlyProductSalesMetric::fromDomain).toList(),
        domain.getDailySales().stream().map(DailyProductSalesMetric::fromDomain).toList()
    );
  }
}
