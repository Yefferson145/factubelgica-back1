package com.factubelgica.api.shared.errors;

public class UserUnauthorizedException extends RuntimeException {
  public UserUnauthorizedException(String message) {
    super(message);
  }
}
