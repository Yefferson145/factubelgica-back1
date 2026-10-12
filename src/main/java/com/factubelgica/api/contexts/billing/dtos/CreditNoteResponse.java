package com.factubelgica.api.contexts.billing.dtos;

import com.factubelgica.api.contexts.billing.models.CreditNote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreditNoteResponse(
    UUID noteId,
    UUID invoiceId,
    Integer noteNumber,
    LocalDate issueDate,
    String reason,
    LocalDateTime created
) {
  public static CreditNoteResponse fromDomainModel(CreditNote creditNote) {
    return new CreditNoteResponse(
        creditNote.getNoteId(),
        creditNote.getInvoiceId(),
        creditNote.getNoteNumber(),
        creditNote.getIssueDate(),
        creditNote.getReason(),
        creditNote.getCreated()
    );
  }
}
