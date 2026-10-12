package com.factubelgica.api.contexts.billing.services;

import com.factubelgica.api.contexts.billing.errors.InvoiceNotFoundException;
import com.factubelgica.api.contexts.billing.models.Invoice;
import com.factubelgica.api.contexts.billing.persistance.IInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceList {

  private final IInvoiceRepository invoiceRepository;

  public List<Invoice> execute(String lastId, int limit, UUID authUserId) {
    boolean isAdmin = SecurityContextHolder.getContext()
        .getAuthentication()
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(a -> a.equals("ROLE_ADMIN"));

    if (isAdmin) {
      return invoiceRepository.findAllPaginated(lastId, limit)
          .orElseThrow(() -> new InvoiceNotFoundException("Could not retrieve invoices"));
    } else {
      return invoiceRepository.findByUserIdPaginated(authUserId, lastId, limit)
          .orElseThrow(() -> new InvoiceNotFoundException("Could not retrieve invoices"));
    }
  }
}
