package com.factubelgica.api.contexts.dashboard.services;

import com.factubelgica.api.contexts.dashboard.errors.DashboardFetchException;
import com.factubelgica.api.contexts.dashboard.errors.ProductNotFoundInSalesException;
import com.factubelgica.api.contexts.dashboard.models.*;
import com.factubelgica.api.contexts.dashboard.persistance.IDashboardRepository;
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
public class ProductSalesReportGet {

  private final IDashboardRepository dashboardRepository;

  public ProductSales execute(int productId, Integer year, Integer month) {
    int targetYear = (year != null) ? year : LocalDate.now().getYear();
    int targetMonth = (month != null) ? month : LocalDate.now().getMonthValue();

    validate(targetYear, targetMonth);

    // Validate product exists — throws 404 if not
    ProductInfo productInfo = dashboardRepository.findProductById(productId)
        .orElseThrow(() -> new ProductNotFoundInSalesException(productId));

    List<RawInvoiceItem> items = dashboardRepository.findRawInvoiceItemsByYear(targetYear)
        .orElseThrow(() -> new DashboardFetchException("Failed to retrieve product metrics for year " + targetYear));

    int daysInMonth = YearMonth.of(targetYear, targetMonth).lengthOfMonth();

    BigDecimal totalQuantityYear = BigDecimal.ZERO;
    BigDecimal totalNetSalesYear = BigDecimal.ZERO;
    BigDecimal totalQuantityMonth = BigDecimal.ZERO;
    BigDecimal totalNetSalesMonth = BigDecimal.ZERO;

    BigDecimal[] monthlyQty = new BigDecimal[12];
    BigDecimal[] monthlyNet = new BigDecimal[12];
    BigDecimal[] dailyQty = new BigDecimal[daysInMonth];
    BigDecimal[] dailyNet = new BigDecimal[daysInMonth];
    Arrays.fill(monthlyQty, BigDecimal.ZERO);
    Arrays.fill(monthlyNet, BigDecimal.ZERO);
    Arrays.fill(dailyQty, BigDecimal.ZERO);
    Arrays.fill(dailyNet, BigDecimal.ZERO);

    for (RawInvoiceItem item : items) {
      if (!item.getProductId().equals(productId)) continue;

      int m = item.getIssueDate().getMonthValue();
      BigDecimal itemQty = item.getQuantity();
      BigDecimal itemNetVal = item.getItemNetPrice().multiply(itemQty);

      totalQuantityYear = totalQuantityYear.add(itemQty);
      totalNetSalesYear = totalNetSalesYear.add(itemNetVal);
      monthlyQty[m - 1] = monthlyQty[m - 1].add(itemQty);
      monthlyNet[m - 1] = monthlyNet[m - 1].add(itemNetVal);

      if (m == targetMonth) {
        int d = item.getIssueDate().getDayOfMonth();
        totalQuantityMonth = totalQuantityMonth.add(itemQty);
        totalNetSalesMonth = totalNetSalesMonth.add(itemNetVal);
        dailyQty[d - 1] = dailyQty[d - 1].add(itemQty);
        dailyNet[d - 1] = dailyNet[d - 1].add(itemNetVal);
      }
    }

    List<MonthlyProductSales> mSales = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      mSales.add(new MonthlyProductSales(i + 1, monthlyQty[i], monthlyNet[i]));
    }

    List<DailyProductSales> dSales = new ArrayList<>();
    for (int i = 0; i < daysInMonth; i++) {
      dSales.add(new DailyProductSales(i + 1, dailyQty[i], dailyNet[i]));
    }

    return new ProductSales(
        productInfo.getProductId(),
        productInfo.getDescription(),
        productInfo.getNetPrice(),
        totalQuantityYear,
        totalNetSalesYear,
        totalQuantityMonth,
        totalNetSalesMonth,
        mSales,
        dSales
    );
  }

  private void validate(int year, int month) {
    if (month < 1 || month > 12) throw new IllegalArgumentException("Month must be between 1 and 12");
    if (year < 1900 || year > 2100) throw new IllegalArgumentException("Year must be between 1900 and 2100");
  }
}
