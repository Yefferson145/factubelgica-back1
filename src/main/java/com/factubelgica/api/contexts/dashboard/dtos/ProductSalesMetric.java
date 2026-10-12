package com.factubelgica.api.contexts.dashboard.dtos;

import com.factubelgica.api.contexts.dashboard.models.ProductSales;
import java.math.BigDecimal;
import java.util.List;

public record ProductSalesMetric(
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
  public static ProductSalesMetric fromDomain(ProductSales domain) {
    return new ProductSalesMetric(
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
