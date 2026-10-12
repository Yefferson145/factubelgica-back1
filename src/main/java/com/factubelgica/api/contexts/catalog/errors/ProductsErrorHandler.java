package com.factubelgica.api.contexts.catalog.errors;

import com.factubelgica.api.shared.errors.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProductsErrorHandler {

  @ExceptionHandler(ErrorCreatingProduct.class)
  public ResponseEntity<ErrorResponse> handleCreatingProduct(ErrorCreatingProduct ecp) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("PRODUCT_CANNOT_BE_CREATED", ecp.getMessage()));
  }

  @ExceptionHandler(ProductsNotFound.class)
  public ResponseEntity<ErrorResponse> handleListingProducts(ProductsNotFound pnf) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("NO_PRODUCTS_FOUND", pnf.getMessage()));
  }
}