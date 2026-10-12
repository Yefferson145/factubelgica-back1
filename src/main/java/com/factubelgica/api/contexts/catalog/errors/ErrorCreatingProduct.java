package com.factubelgica.api.contexts.catalog.errors;

public class ErrorCreatingProduct extends RuntimeException {
  public ErrorCreatingProduct(String message) {
    super(message);
  }
}
