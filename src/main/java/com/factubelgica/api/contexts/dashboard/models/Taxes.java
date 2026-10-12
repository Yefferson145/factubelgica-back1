package com.factubelgica.api.contexts.dashboard.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class Taxes {
  private final BigDecimal totalTaxesYear;
  private final BigDecimal totalTaxesMonth;
  private final List<MonthlyTax> yearlyTaxes;
  private final List<DailyTax> monthlyTaxes;
}
