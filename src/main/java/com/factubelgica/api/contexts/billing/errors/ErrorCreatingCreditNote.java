package com.factubelgica.api.contexts.billing.errors;

public class ErrorCreatingCreditNote extends RuntimeException {
  public ErrorCreatingCreditNote(String message) {
    super(message);
  }
}
