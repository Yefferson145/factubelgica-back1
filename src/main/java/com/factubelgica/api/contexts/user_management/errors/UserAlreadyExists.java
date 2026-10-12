package com.factubelgica.api.contexts.user_management.errors;

public class UserAlreadyExists extends RuntimeException {
  public UserAlreadyExists(String message) {
    super(message);
  }
}
