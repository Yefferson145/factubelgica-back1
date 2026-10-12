package com.factubelgica.api.contexts.billing.errors;

public class InvoiceAnnulledException extends RuntimeException {
  public InvoiceAnnulledException(String message) {
    super(message);
  }
}
