package com.factubelgica.api.contexts.catalog.errors;

public class ProductsNotFound extends RuntimeException {
  public ProductsNotFound(String message) {
    super(message);
  }
}
