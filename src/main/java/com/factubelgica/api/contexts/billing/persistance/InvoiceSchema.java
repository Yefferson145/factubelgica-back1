package com.factubelgica.api.contexts.billing.persistance;

import com.factubelgica.api.contexts.billing.models.Invoice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceSchema {

  @Id
  @Column(name = "invoice_id", columnDefinition = "uuid")
  private UUID invoiceId;

  @Column(name = "info_id", nullable = false)
  private Integer infoId;

  @Column(name = "user_id", columnDefinition = "uuid", nullable = false)
  private UUID userId;

  @Column(name = "invoice_number", insertable = false, updatable = false)
  private Integer invoiceNumber;

  @Column(name = "issue_date", nullable = false)
  private LocalDate issueDate;

  @Column(name = "due_date", nullable = false)
  private LocalDate dueDate;

  @Column(name = "vat_rate", nullable = false, precision = 5, scale = 2)
  private BigDecimal vatRate;

  @Column(name = "payment_date")
  private LocalDate paymentDate;

  @Column(nullable = false)
  private String status;

  @Column(insertable = false, updatable = false)
  private LocalDateTime created;

  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<InvoiceItemSchema> items = new ArrayList<>();

  public static InvoiceSchema fromDomainModel(Invoice invoice) {
    InvoiceSchema entity = new InvoiceSchema();
    entity.setInvoiceId(invoice.getInvoiceId());
    entity.setInfoId(invoice.getInfoId());
    entity.setUserId(invoice.getUserId());
    entity.setIssueDate(invoice.getIssueDate());
    entity.setDueDate(invoice.getDueDate());
    entity.setVatRate(invoice.getVatRate());
    entity.setPaymentDate(invoice.getPaymentDate());
    entity.setStatus(invoice.getStatus());
    return entity;
  }

  public Invoice toDomainModel() {
    List<com.factubelgica.api.contexts.billing.models.InvoiceItem> domainItems = this.items
        .stream()
        .map(InvoiceItemSchema::toDomainModel)
        .collect(Collectors.toList());

    return new Invoice(
        this.invoiceId,
        this.infoId,
        this.userId,
        this.invoiceNumber,
        this.issueDate,
        this.dueDate,
        this.vatRate,
        this.paymentDate,
        this.status,
        this.created,
        domainItems
    );
  }
}
