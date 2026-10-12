package com.factubelgica.api.contexts.dashboard.services;

import com.factubelgica.api.contexts.dashboard.errors.*;
import com.factubelgica.api.contexts.dashboard.models.*;
import com.factubelgica.api.contexts.dashboard.persistance.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxReportGet {

  private final IDashboardRepository dashboardRepository;

  public Taxes execute(Integer year, Integer month) {
    int targetYear = (year != null) ? year : LocalDate.now().getYear();
    int targetMonth = (month != null) ? month : LocalDate.now().getMonthValue();

    validate(targetYear, targetMonth);

    List<RawInvoiceItem> items = dashboardRepository.findRawInvoiceItemsByYear(targetYear)
        .orElseThrow(() -> new DashboardFetchException("Failed to retrieve tax metrics for year " + targetYear));

    int daysInMonth = YearMonth.of(targetYear, targetMonth).lengthOfMonth();

    BigDecimal[] taxesByMonth = new BigDecimal[12];
    BigDecimal[] taxesByDay = new BigDecimal[daysInMonth];
    Arrays.fill(taxesByMonth, BigDecimal.ZERO);
    Arrays.fill(taxesByDay, BigDecimal.ZERO);

    for (RawInvoiceItem item : items) {
      int m = item.getIssueDate().getMonthValue();
      BigDecimal itemNetVal = item.getItemNetPrice().multiply(item.getQuantity());
      BigDecimal taxMultiplier = item.getVatRate().divide(BigDecimal.valueOf(100));
      BigDecimal itemTaxVal = itemNetVal.multiply(taxMultiplier);

      taxesByMonth[m - 1] = taxesByMonth[m - 1].add(itemTaxVal);

      if (m == targetMonth) {
        int d = item.getIssueDate().getDayOfMonth();
        taxesByDay[d - 1] = taxesByDay[d - 1].add(itemTaxVal);
      }
    }

    List<MonthlyTax> yearlyTaxesList = new ArrayList<>();
    BigDecimal totalTaxesYear = BigDecimal.ZERO;
    for (int i = 0; i < 12; i++) {
      yearlyTaxesList.add(new MonthlyTax(i + 1, taxesByMonth[i]));
      totalTaxesYear = totalTaxesYear.add(taxesByMonth[i]);
    }

    List<DailyTax> monthlyTaxesList = new ArrayList<>();
    BigDecimal totalTaxesMonth = BigDecimal.ZERO;
    for (int i = 0; i < daysInMonth; i++) {
      monthlyTaxesList.add(new DailyTax(i + 1, taxesByDay[i]));
      totalTaxesMonth = totalTaxesMonth.add(taxesByDay[i]);
    }

    return new Taxes(
        totalTaxesYear,
        totalTaxesMonth,
        yearlyTaxesList,
        monthlyTaxesList
    );
  }

  private void validate(int year, int month) {
    if (month < 1 || month > 12) throw new IllegalArgumentException("Month must be between 1 and 12");
    if (year < 1900 || year > 2100) throw new IllegalArgumentException("Year must be between 1900 and 2100");
  }
}
