package com.factubelgica.api.contexts.billing.dtos;

import com.factubelgica.api.contexts.billing.models.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record InvoiceCreateResponse(
    UUID invoiceId,
    Integer invoiceNumber,
    Integer infoId,
    UUID userId,
    LocalDate issueDate,
    LocalDate dueDate,
    BigDecimal vatRate,
    LocalDate paymentDate,
    String status,
    LocalDateTime created,
    List<InvoiceItemResponse> items,
    BigDecimal totalNet,
    BigDecimal totalVat,
    BigDecimal totalGross
) {
  public static InvoiceCreateResponse fromDomainModel(Invoice invoice) {
    List<InvoiceItemResponse> itemResponses = invoice.getItems()
        .stream()
        .map(InvoiceItemResponse::fromDomainModel)
        .toList();

    BigDecimal totalNet = itemResponses.stream()
        .map(InvoiceItemResponse::lineTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalVat = totalNet.multiply(invoice.getVatRate());
    BigDecimal totalGross = totalNet.add(totalVat);

    return new InvoiceCreateResponse(
        invoice.getInvoiceId(),
        invoice.getInvoiceNumber(),
        invoice.getInfoId(),
        invoice.getUserId(),
        invoice.getIssueDate(),
        invoice.getDueDate(),
        invoice.getVatRate(),
        invoice.getPaymentDate(),
        invoice.getStatus(),
        invoice.getCreated(),
        itemResponses,
        totalNet,
        totalVat,
        totalGross
    );
  }
}
