package com.factubelgica.api.contexts.dashboard.controllers;

import com.factubelgica.api.contexts.dashboard.dtos.ProductSalesReportResponse;
import com.factubelgica.api.contexts.dashboard.dtos.SalesReportResponse;
import com.factubelgica.api.contexts.dashboard.dtos.TaxReportResponse;
import com.factubelgica.api.contexts.dashboard.models.ProductSales;
import com.factubelgica.api.contexts.dashboard.models.SalesReport;
import com.factubelgica.api.contexts.dashboard.models.Taxes;
import com.factubelgica.api.contexts.dashboard.services.ProductSalesReportGet;
import com.factubelgica.api.contexts.dashboard.services.SalesReportGet;
import com.factubelgica.api.contexts.dashboard.services.TaxReportGet;
import com.factubelgica.api.shared.core.JwtService;
import com.factubelgica.api.shared.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("a-role/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

  private final SalesReportGet salesReportGet;
  private final ProductSalesReportGet productSalesReportGet;
  private final TaxReportGet taxReportGet;
  private final CookieUtil cookieUtil;
  private final JwtService jwtService;

  @GetMapping("/sales")
  public ResponseEntity<SalesReportResponse> getSalesReport(
      @RequestParam(value = "year", required = false) Integer year,
      @RequestParam(value = "month", required = false) Integer month,
      @AuthenticationPrincipal UUID authUserId
  ) {
    SalesReport report = salesReportGet.execute(year, month);
    SalesReportResponse response = SalesReportResponse.fromDomain(report);

    return ResponseEntity
        .ok()
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil.createTokenCookie(jwtService.generateToken(authUserId)).toString()
        )
        .body(response);
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<ProductSalesReportResponse> getProductSalesReport(
      @PathVariable("id") int productId,
      @RequestParam(value = "year", required = false) Integer year,
      @RequestParam(value = "month", required = false) Integer month,
      @AuthenticationPrincipal UUID authUserId
  ) {
    ProductSales report = productSalesReportGet.execute(productId, year, month);
    int targetYear = (year != null) ? year : java.time.LocalDate.now().getYear();
    int targetMonth = (month != null) ? month : java.time.LocalDate.now().getMonthValue();
    ProductSalesReportResponse response = ProductSalesReportResponse.fromDomain(targetYear, targetMonth, report);

    return ResponseEntity
        .ok()
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil.createTokenCookie(jwtService.generateToken(authUserId)).toString()
        )
        .body(response);
  }

  @GetMapping("/taxes")
  public ResponseEntity<TaxReportResponse> getTaxReport(
      @RequestParam(value = "year", required = false) Integer year,
      @RequestParam(value = "month", required = false) Integer month,
      @AuthenticationPrincipal UUID authUserId
  ) {
    Taxes report = taxReportGet.execute(year, month);
    int targetYear = (year != null) ? year : java.time.LocalDate.now().getYear();
    int targetMonth = (month != null) ? month : java.time.LocalDate.now().getMonthValue();
    TaxReportResponse response = TaxReportResponse.fromDomain(targetYear, targetMonth, report);

    return ResponseEntity
        .ok()
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil.createTokenCookie(jwtService.generateToken(authUserId)).toString()
        )
        .body(response);
  }
}
