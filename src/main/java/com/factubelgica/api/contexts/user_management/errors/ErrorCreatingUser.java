package com.factubelgica.api.contexts.user_management.errors;

public class ErrorCreatingUser extends RuntimeException {
  public ErrorCreatingUser(String message) {
    super(message);
  }
}
