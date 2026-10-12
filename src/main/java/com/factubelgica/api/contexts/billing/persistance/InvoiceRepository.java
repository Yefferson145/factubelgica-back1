package com.factubelgica.api.contexts.billing.persistance;

import com.factubelgica.api.contexts.billing.models.Invoice;
import com.factubelgica.api.shared.utils.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InvoiceRepository implements IInvoiceRepository {

  private final JpaInvoiceRepository jpaInvoiceRepository;

  @Override
  @Transactional
  public Optional<Invoice> save(Invoice invoice) {
    try {
      InvoiceSchema entity = InvoiceSchema.fromDomainModel(invoice);

      // Build and link each item to the invoice entity before saving
      for (com.factubelgica.api.contexts.billing.models.InvoiceItem item : invoice.getItems()) {
        InvoiceItemSchema itemSchema = InvoiceItemSchema.fromDomainModel(item, entity);
        entity.getItems().add(itemSchema);
      }

      InvoiceSchema saved = jpaInvoiceRepository.save(entity);
      // Reload to get the DB-generated invoice_number and created timestamp
      return jpaInvoiceRepository.findById(saved.getInvoiceId())
          .map(InvoiceSchema::toDomainModel);
    } catch (Exception e) {
      Slf4j.logger.warn("Error saving invoice for infoId: {}", invoice.getInfoId(), e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Invoice> findById(UUID invoiceId) {
    try {
      return jpaInvoiceRepository.findById(invoiceId)
          .map(InvoiceSchema::toDomainModel);
    } catch (Exception e) {
      Slf4j.logger.warn("Error finding invoice by ID: {}", invoiceId, e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<Invoice>> findByUserIdPaginated(UUID userId, String lastId, int limit) {
    try {
      return Optional.of(
          jpaInvoiceRepository.findInvoicesByUserIdPaginated(userId, lastId, limit)
              .stream()
              .map(InvoiceSchema::toDomainModel)
              .toList()
      );
    } catch (Exception e) {
      Slf4j.logger.warn("Error fetching invoices for userId: {} from lastId: {}", userId, lastId, e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<Invoice>> findAllPaginated(String lastId, int limit) {
    try {
      return Optional.of(
          jpaInvoiceRepository.findAllInvoicesPaginated(lastId, limit)
              .stream()
              .map(InvoiceSchema::toDomainModel)
              .toList()
      );
    } catch (Exception e) {
      Slf4j.logger.warn("Error fetching all invoices paginated from lastId: {}", lastId, e);
      return Optional.empty();
    }
  }

  @Override
  @Transactional
  public void updateStatus(UUID invoiceId, String status, LocalDate paymentDate) {
    jpaInvoiceRepository.updateStatus(invoiceId.toString(), status, paymentDate);
  }
}
