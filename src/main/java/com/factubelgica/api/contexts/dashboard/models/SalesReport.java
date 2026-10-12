package com.factubelgica.api.contexts.dashboard.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class SalesReport {
  private final int year;
  private final int month;
  private final BigDecimal totalNetSalesYear;
  private final BigDecimal totalGrossSalesYear;
  private final BigDecimal totalNetSalesMonth;
  private final BigDecimal totalGrossSalesMonth;
  private final List<MonthlySales> yearlySales;
  private final List<DailySales> monthlySales;
}
