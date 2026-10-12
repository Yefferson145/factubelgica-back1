package com.factubelgica.api.contexts.billing.services;

import com.factubelgica.api.contexts.billing.dtos.InvoiceStatusUpdateRequest;
import com.factubelgica.api.contexts.billing.errors.InvalidInvoiceTransitionException;
import com.factubelgica.api.contexts.billing.errors.InvoiceAnnulledException;
import com.factubelgica.api.contexts.billing.errors.InvoiceNotFoundException;
import com.factubelgica.api.contexts.billing.models.Invoice;
import com.factubelgica.api.contexts.billing.persistance.IInvoiceRepository;
import com.factubelgica.api.shared.errors.UserUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceStatusUpdate {

  private final IInvoiceRepository invoiceRepository;

  @Transactional
  public Invoice execute(UUID invoiceId, InvoiceStatusUpdateRequest req, UUID authUserId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with ID: " + invoiceId));

    // USER role can only manage their own invoices; ADMIN can manage all
    boolean isAdmin = SecurityContextHolder.getContext()
        .getAuthentication()
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(a -> a.equals("ROLE_ADMIN"));

    if (!isAdmin && !invoice.getUserId().equals(authUserId)) {
      throw new UserUnauthorizedException("You are not authorized to update this invoice");
    }

    String currentStatus = invoice.getStatus();
    String targetStatus = req.status();

    if ("Anulado".equals(currentStatus)) {
      throw new InvoiceAnnulledException("Invoice is annulled and cannot be modified.");
    }

    // Progression verification: Pendiente -> Pagado -> Enviado
    LocalDate paymentDate = invoice.getPaymentDate();

    if (!currentStatus.equals(targetStatus)) {
      if ("Pendiente".equals(currentStatus)) {
        if ("Pagado".equals(targetStatus)) {
          paymentDate = LocalDate.now();
        } else {
          throw new InvalidInvoiceTransitionException("Invalid transition from " + currentStatus + " to " + targetStatus);
        }
      } else if ("Pagado".equals(currentStatus)) {
        if (!"Enviado".equals(targetStatus)) {
          throw new InvalidInvoiceTransitionException("Invalid transition from " + currentStatus + " to " + targetStatus);
        }
      } else if ("Enviado".equals(currentStatus)) {
        throw new InvalidInvoiceTransitionException("Invalid transition from " + currentStatus + " to " + targetStatus);
      } else {
        throw new InvalidInvoiceTransitionException("Invalid transition from " + currentStatus + " to " + targetStatus);
      }
    }

    invoiceRepository.updateStatus(invoiceId, targetStatus, paymentDate);

    return invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found after update with ID: " + invoiceId));
  }
}
