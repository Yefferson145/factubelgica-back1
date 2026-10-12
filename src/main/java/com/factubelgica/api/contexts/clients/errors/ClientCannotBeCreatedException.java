package com.factubelgica.api.contexts.clients.errors;

public class ClientCannotBeCreatedException extends RuntimeException {
  public ClientCannotBeCreatedException(String message) {
    super(message);
  }
}
