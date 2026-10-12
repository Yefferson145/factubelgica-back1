package com.factubelgica.api.contexts.dashboard.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ProductSales {
  private final int productId;
  private final String description;
  private final BigDecimal currentNetPrice;
  private final BigDecimal totalQuantityYear;
  private final BigDecimal totalNetSalesYear;
  private final BigDecimal totalQuantityMonth;
  private final BigDecimal totalNetSalesMonth;
  private final List<MonthlyProductSales> monthlySales;
  private final List<DailyProductSales> dailySales;
}
