package com.factubelgica.api.contexts.clients.errors;

public class ClientVatAlreadyExistsException extends RuntimeException {
  public ClientVatAlreadyExistsException(String message) {
    super(message);
  }
}
