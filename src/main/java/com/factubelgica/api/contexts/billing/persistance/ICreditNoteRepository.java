package com.factubelgica.api.contexts.billing.persistance;

import com.factubelgica.api.contexts.billing.models.CreditNote;

import java.util.List;
import java.util.Optional;

public interface ICreditNoteRepository {
  Optional<CreditNote> save(CreditNote creditNote);
  Optional<List<CreditNote>> findAllPaginated(String lastId, int limit);
}
