package com.factubelgica.api.contexts.user_management.errors;

public class UsersNotFound extends RuntimeException {
  public UsersNotFound(String message) {
    super(message);
  }
}
