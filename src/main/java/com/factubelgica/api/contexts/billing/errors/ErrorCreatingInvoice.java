package com.factubelgica.api.contexts.billing.errors;

public class ErrorCreatingInvoice extends RuntimeException {
  public ErrorCreatingInvoice(String message) {
    super(message);
  }
}
