package com.factubelgica.api.contexts.billing.services;

import com.factubelgica.api.contexts.billing.dtos.CreditNoteCreateRequest;
import com.factubelgica.api.contexts.billing.errors.ErrorCreatingCreditNote;
import com.factubelgica.api.contexts.billing.errors.InvoiceAnnulledException;
import com.factubelgica.api.contexts.billing.errors.InvoiceNotFoundException;
import com.factubelgica.api.contexts.billing.models.CreditNote;
import com.factubelgica.api.contexts.billing.models.Invoice;
import com.factubelgica.api.contexts.billing.persistance.ICreditNoteRepository;
import com.factubelgica.api.contexts.billing.persistance.IInvoiceRepository;
import com.factubelgica.api.shared.core.UUIDv7;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditNoteCreate {

  private final ICreditNoteRepository creditNoteRepository;
  private final IInvoiceRepository invoiceRepository;
  private final UUIDv7 uuidv7;

  @Transactional
  public CreditNote execute(CreditNoteCreateRequest req) {
    Invoice invoice = invoiceRepository.findById(req.invoiceId())
        .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with ID: " + req.invoiceId()));

    if ("Anulado".equals(invoice.getStatus())) {
      throw new InvoiceAnnulledException("Invoice is already annulled.");
    }

    // Create the Credit Note domain model
    CreditNote creditNote = new CreditNote(
        uuidv7.generate(),
        invoice.getInvoiceId(),
        null, // noteNumber — handled by DB trigger
        LocalDate.now(),
        req.reason(),
        null // created — handled by DB
    );

    // Update invoice status to Anulado
    invoiceRepository.updateStatus(invoice.getInvoiceId(), "Anulado", invoice.getPaymentDate());

    // Save credit note
    return creditNoteRepository.save(creditNote)
        .orElseThrow(() -> new ErrorCreatingCreditNote("Credit Note could not be saved"));
  }
}
