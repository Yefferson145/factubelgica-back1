package com.factubelgica.api.contexts.clients.persistance;

import com.factubelgica.api.contexts.clients.models.Client;
import java.util.List;
import java.util.Optional;

public interface IClientRepository {
  Optional<Client> save(Client client);
  Optional<Client> findById(Integer clientId);
  Optional<Client> findByVatNumber(String vatNumber);
  Optional<List<Client>> getClientsPaginated(Integer lastId, int limit);
}
