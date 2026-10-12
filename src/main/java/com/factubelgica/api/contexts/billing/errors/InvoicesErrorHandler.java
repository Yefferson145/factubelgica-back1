package com.factubelgica.api.contexts.billing.errors;

import com.factubelgica.api.shared.errors.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InvoicesErrorHandler {

  @ExceptionHandler(InvoiceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleInvoiceNotFound(InvoiceNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("INVOICE_NOT_FOUND", e.getMessage()));
  }

  @ExceptionHandler(InvalidInvoiceTransitionException.class)
  public ResponseEntity<ErrorResponse> handleInvalidTransition(InvalidInvoiceTransitionException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("INVALID_STATUS_TRANSITION", e.getMessage()));
  }

  @ExceptionHandler(InvoiceAnnulledException.class)
  public ResponseEntity<ErrorResponse> handleAnnulled(InvoiceAnnulledException e) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(new ErrorResponse("INVOICE_ANNULLED", e.getMessage()));
  }

  @ExceptionHandler(ErrorCreatingInvoice.class)
  public ResponseEntity<ErrorResponse> handleCreatingInvoice(ErrorCreatingInvoice e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("INVOICE_CANNOT_BE_CREATED", e.getMessage()));
  }

  @ExceptionHandler(ErrorCreatingCreditNote.class)
  public ResponseEntity<ErrorResponse> handleCreatingCreditNote(ErrorCreatingCreditNote e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("CREDIT_NOTE_CANNOT_BE_CREATED", e.getMessage()));
  }
}
