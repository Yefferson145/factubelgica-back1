package com.factubelgica.api.contexts.billing.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class CreditNote {
  private final UUID noteId;
  private final UUID invoiceId;
  private final Integer noteNumber;
  private final LocalDate issueDate;
  private final String reason;
  private final LocalDateTime created;
}
