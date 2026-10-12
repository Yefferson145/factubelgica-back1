package com.factubelgica.api.contexts.billing.errors;

public class InvalidInvoiceTransitionException extends RuntimeException {
  public InvalidInvoiceTransitionException(String message) {
    super(message);
  }
}
