package com.factubelgica.api.contexts.user_management.errors;

import com.factubelgica.api.shared.errors.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthErrorHandler {
  @ExceptionHandler(UserAlreadyExists.class)
  public ResponseEntity<ErrorResponse> handleUserExists(UserAlreadyExists uae) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(new ErrorResponse("USER_ALREADY_EXISTS", uae.getMessage()));
  }
}
