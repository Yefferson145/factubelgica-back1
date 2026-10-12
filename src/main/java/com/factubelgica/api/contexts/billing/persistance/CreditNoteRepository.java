package com.factubelgica.api.contexts.billing.persistance;

import com.factubelgica.api.contexts.billing.models.CreditNote;
import com.factubelgica.api.shared.utils.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreditNoteRepository implements ICreditNoteRepository {

  private final JpaCreditNoteRepository jpaCreditNoteRepository;

  @Override
  @Transactional
  public Optional<CreditNote> save(CreditNote creditNote) {
    try {
      CreditNoteSchema entity = CreditNoteSchema.fromDomainModel(creditNote);
      CreditNoteSchema saved = jpaCreditNoteRepository.save(entity);
      // Reload to pick up DB-trigger-generated note_number
      return jpaCreditNoteRepository.findById(saved.getNoteId())
          .map(CreditNoteSchema::toDomainModel);
    } catch (Exception e) {
      Slf4j.logger.warn("Error saving credit note for invoiceId: {}", creditNote.getInvoiceId(), e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<CreditNote>> findAllPaginated(String lastId, int limit) {
    try {
      return Optional.of(
          jpaCreditNoteRepository.findCreditNotesPaginated(lastId, limit)
              .stream()
              .map(CreditNoteSchema::toDomainModel)
              .toList()
      );
    } catch (Exception e) {
      Slf4j.logger.warn("Error fetching credit notes paginated from lastId: {}", lastId, e);
      return Optional.empty();
    }
  }
}
