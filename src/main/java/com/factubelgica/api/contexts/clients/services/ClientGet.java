package com.factubelgica.api.contexts.clients.services;

import com.factubelgica.api.contexts.clients.errors.ClientNotFoundException;
import com.factubelgica.api.contexts.clients.models.Client;
import com.factubelgica.api.contexts.clients.persistance.IClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientGet {

  private final IClientRepository repository;

  public Client execute(Integer clientId) {
    return repository
        .findById(clientId)
        .orElseThrow(() -> new ClientNotFoundException("Client not found with ID: " + clientId));
  }
}
