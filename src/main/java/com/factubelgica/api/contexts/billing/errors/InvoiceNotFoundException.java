package com.factubelgica.api.contexts.billing.errors;

public class InvoiceNotFoundException extends RuntimeException {
  public InvoiceNotFoundException(String message) {
    super(message);
  }
}
