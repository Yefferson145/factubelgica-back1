package com.factubelgica.api.contexts.clients.services;

import com.factubelgica.api.contexts.clients.dtos.ClientCreateRequest;
import com.factubelgica.api.contexts.clients.errors.ClientCannotBeCreatedException;
import com.factubelgica.api.contexts.clients.errors.ClientVatAlreadyExistsException;
import com.factubelgica.api.contexts.clients.models.Client;
import com.factubelgica.api.contexts.clients.persistance.IClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientCreate {

  private final IClientRepository repository;

  public Client execute(ClientCreateRequest req) {
    if (repository.findByVatNumber(req.vatNumber().strip()).isPresent()) {
      throw new ClientVatAlreadyExistsException("VAT number already exists: " + req.vatNumber());
    }

    Client clientToSave = req.toClient(0);

    return repository.save(clientToSave)
        .orElseThrow(() -> new ClientCannotBeCreatedException("Client cannot be created"));
  }
}
