package com.factubelgica.api.contexts.clients.services;

import com.factubelgica.api.contexts.clients.errors.ClientNotFoundException;
import com.factubelgica.api.contexts.clients.models.Client;
import com.factubelgica.api.contexts.clients.persistance.IClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientList {

  private final IClientRepository repository;

  public List<Client> execute(Integer lastId, int limit) {
    return repository
        .getClientsPaginated(lastId, limit)
        .filter(list -> !list.isEmpty())
        .orElseThrow(() -> new ClientNotFoundException("No active clients found from lastId: " + lastId));
  }
}
