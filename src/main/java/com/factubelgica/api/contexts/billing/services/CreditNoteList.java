package com.factubelgica.api.contexts.billing.services;

import com.factubelgica.api.contexts.billing.models.CreditNote;
import com.factubelgica.api.contexts.billing.persistance.ICreditNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditNoteList {

  private final ICreditNoteRepository creditNoteRepository;

  public List<CreditNote> execute(String lastId, int limit) {
    return creditNoteRepository.findAllPaginated(lastId, limit)
        .orElseThrow(() -> new RuntimeException("Could not retrieve credit notes"));
  }
}
