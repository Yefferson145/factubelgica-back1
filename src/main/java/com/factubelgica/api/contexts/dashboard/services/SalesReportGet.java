package com.factubelgica.api.contexts.dashboard.services;

import com.factubelgica.api.contexts.dashboard.errors.*;
import com.factubelgica.api.contexts.dashboard.models.*;
import com.factubelgica.api.contexts.dashboard.persistance.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesReportGet {

  private final IDashboardRepository dashboardRepository;

  public SalesReport execute(Integer year, Integer month) {
    int targetYear = (year != null) ? year : LocalDate.now().getYear();
    int targetMonth = (month != null) ? month : LocalDate.now().getMonthValue();

    validate(targetYear, targetMonth);

    List<RawInvoiceItem> items = dashboardRepository.findRawInvoiceItemsByYear(targetYear)
        .orElseThrow(() -> new DashboardFetchException("Failed to retrieve sales metrics for year " + targetYear));

    int daysInMonth = YearMonth.of(targetYear, targetMonth).lengthOfMonth();

    BigDecimal[] netSalesByMonth = new BigDecimal[12];
    BigDecimal[] grossSalesByMonth = new BigDecimal[12];
    Arrays.fill(netSalesByMonth, BigDecimal.ZERO);
    Arrays.fill(grossSalesByMonth, BigDecimal.ZERO);

    BigDecimal[] netSalesByDay = new BigDecimal[daysInMonth];
    BigDecimal[] grossSalesByDay = new BigDecimal[daysInMonth];
    Arrays.fill(netSalesByDay, BigDecimal.ZERO);
    Arrays.fill(grossSalesByDay, BigDecimal.ZERO);

    for (RawInvoiceItem item : items) {
      int m = item.getIssueDate().getMonthValue();
      BigDecimal itemNetVal = item.getItemNetPrice().multiply(item.getQuantity());
      BigDecimal itemGrossVal = itemNetVal.add(itemNetVal.multiply(item.getVatRate().divide(BigDecimal.valueOf(100))));

      netSalesByMonth[m - 1] = netSalesByMonth[m - 1].add(itemNetVal);
      grossSalesByMonth[m - 1] = grossSalesByMonth[m - 1].add(itemGrossVal);

      if (m == targetMonth) {
        int d = item.getIssueDate().getDayOfMonth();
        netSalesByDay[d - 1] = netSalesByDay[d - 1].add(itemNetVal);
        grossSalesByDay[d - 1] = grossSalesByDay[d - 1].add(itemGrossVal);
      }
    }

    List<MonthlySales> yearlySalesList = new ArrayList<>();
    BigDecimal totalNetSalesYear = BigDecimal.ZERO;
    BigDecimal totalGrossSalesYear = BigDecimal.ZERO;
    for (int i = 0; i < 12; i++) {
      yearlySalesList.add(new MonthlySales(i + 1, netSalesByMonth[i], grossSalesByMonth[i]));
      totalNetSalesYear = totalNetSalesYear.add(netSalesByMonth[i]);
      totalGrossSalesYear = totalGrossSalesYear.add(grossSalesByMonth[i]);
    }

    List<DailySales> monthlySalesList = new ArrayList<>();
    BigDecimal totalNetSalesMonth = BigDecimal.ZERO;
    BigDecimal totalGrossSalesMonth = BigDecimal.ZERO;
    for (int i = 0; i < daysInMonth; i++) {
      monthlySalesList.add(new DailySales(i + 1, netSalesByDay[i], grossSalesByDay[i]));
      totalNetSalesMonth = totalNetSalesMonth.add(netSalesByDay[i]);
      totalGrossSalesMonth = totalGrossSalesMonth.add(grossSalesByDay[i]);
    }

    return new SalesReport(
        targetYear,
        targetMonth,
        totalNetSalesYear,
        totalGrossSalesYear,
        totalNetSalesMonth,
        totalGrossSalesMonth,
        yearlySalesList,
        monthlySalesList
    );
  }

  private void validate(int year, int month) {
    if (month < 1 || month > 12) throw new IllegalArgumentException("Month must be between 1 and 12");
    if (year < 1900 || year > 2100) throw new IllegalArgumentException("Year must be between 1900 and 2100");
  }
}
