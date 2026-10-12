package com.factubelgica.api.contexts.billing.persistance;

import com.factubelgica.api.contexts.billing.models.Invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IInvoiceRepository {
  Optional<Invoice> save(Invoice invoice);
  Optional<Invoice> findById(UUID invoiceId);
  Optional<List<Invoice>> findByUserIdPaginated(UUID userId, String lastId, int limit);
  Optional<List<Invoice>> findAllPaginated(String lastId, int limit);
  void updateStatus(UUID invoiceId, String status, LocalDate paymentDate);
}
