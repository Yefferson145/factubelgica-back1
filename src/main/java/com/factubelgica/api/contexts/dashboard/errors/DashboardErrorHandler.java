package com.factubelgica.api.contexts.dashboard.errors;

import com.factubelgica.api.shared.errors.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DashboardErrorHandler {

  @ExceptionHandler(DashboardFetchException.class)
  public ResponseEntity<ErrorResponse> handleDashboardFetch(DashboardFetchException e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("DASHBOARD_FETCH_ERROR", e.getMessage()));
  }

  @ExceptionHandler(ProductNotFoundInSalesException.class)
  public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundInSalesException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("PRODUCT_NOT_FOUND", e.getMessage()));
  }
}
