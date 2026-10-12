package com.factubelgica.api.contexts.billing.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Invoice {
  private final UUID invoiceId;
  private final Integer infoId;       // snapshot of client_mutable at time of billing
  private final UUID userId;
  private final Integer invoiceNumber;
  private final LocalDate issueDate;
  private final LocalDate dueDate;
  private final BigDecimal vatRate;
  private final LocalDate paymentDate;
  private final String status;
  private final LocalDateTime created;
  private final List<InvoiceItem> items;
}
