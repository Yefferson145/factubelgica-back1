package com.factubelgica.api.contexts.clients.services;

import com.factubelgica.api.contexts.clients.dtos.ClientUpdateRequest;
import com.factubelgica.api.contexts.clients.errors.ClientNotFoundException;
import com.factubelgica.api.contexts.clients.models.Client;
import com.factubelgica.api.contexts.clients.persistance.IClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientUpdate {

  private final IClientRepository repository;

  public Client execute(Integer clientId, ClientUpdateRequest req) {
    Client existingClient = repository.findById(clientId)
        .orElseThrow(() -> new ClientNotFoundException("Client not found with ID: " + clientId));

    Client updatedClient = req.toClient(clientId, existingClient.getVatNumber());

    return repository.save(updatedClient)
        .orElseThrow(() -> new RuntimeException("Error updating client with ID: " + clientId));
  }
}
