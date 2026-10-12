package com.factubelgica.api.contexts.billing.services;

import com.factubelgica.api.contexts.billing.errors.InvoiceNotFoundException;
import com.factubelgica.api.contexts.billing.models.Invoice;
import com.factubelgica.api.contexts.billing.persistance.IInvoiceRepository;
import com.factubelgica.api.shared.errors.UserUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceGet {

  private final IInvoiceRepository invoiceRepository;

  public Invoice execute(UUID invoiceId, UUID authUserId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new InvoiceNotFoundException(
            "Invoice not found with ID: " + invoiceId));

    // USER role can only view their own invoices; ADMIN can view all
    boolean isAdmin = SecurityContextHolder.getContext()
        .getAuthentication()
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(a -> a.equals("ROLE_ADMIN"));

    if (!isAdmin && !invoice.getUserId().equals(authUserId)) {
      throw new UserUnauthorizedException("You are not authorized to view this invoice");
    }

    return invoice;
  }
}
