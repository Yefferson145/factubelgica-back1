package com.factubelgica.api.contexts.dashboard.errors;

public class ProductNotFoundInSalesException extends RuntimeException {
  public ProductNotFoundInSalesException(int productId) {
    super("No product found with id: " + productId);
  }
}
