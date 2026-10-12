package com.factubelgica.api.contexts.clients.errors;

import com.factubelgica.api.shared.errors.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClientsErrorHandler {

  @ExceptionHandler(ClientCannotBeCreatedException.class)
  public ResponseEntity<ErrorResponse> handleCannotBeCreated(ClientCannotBeCreatedException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("CLIENT_CANNOT_BE_CREATED", ex.getMessage()));
  }

  @ExceptionHandler(ClientNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ClientNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("CLIENT_NOT_FOUND", ex.getMessage()));
  }

  @ExceptionHandler(ClientVatAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleVatExists(ClientVatAlreadyExistsException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("CLIENT_VAT_ALREADY_EXISTS", ex.getMessage()));
  }
}
