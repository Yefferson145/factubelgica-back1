package com.factubelgica.api.contexts.billing.persistance;

import com.factubelgica.api.contexts.billing.models.CreditNote;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "credit_notes")
@Getter
@Setter
@NoArgsConstructor
public class CreditNoteSchema {

  @Id
  @Column(name = "note_id", columnDefinition = "uuid")
  private UUID noteId;

  @Column(name = "invoice_id", columnDefinition = "uuid", nullable = false)
  private UUID invoiceId;

  @Column(name = "note_number", insertable = false, updatable = false)
  private Integer noteNumber;

  @Column(name = "issue_date", nullable = false)
  private LocalDate issueDate;

  @Column
  private String reason;

  @Column(insertable = false, updatable = false)
  private LocalDateTime created;

  public static CreditNoteSchema fromDomainModel(CreditNote creditNote) {
    CreditNoteSchema entity = new CreditNoteSchema();
    entity.setNoteId(creditNote.getNoteId());
    entity.setInvoiceId(creditNote.getInvoiceId());
    entity.setIssueDate(creditNote.getIssueDate());
    entity.setReason(creditNote.getReason());
    return entity;
  }

  public CreditNote toDomainModel() {
    return new CreditNote(
        this.noteId,
        this.invoiceId,
        this.noteNumber,
        this.issueDate,
        this.reason,
        this.created
    );
  }
}
