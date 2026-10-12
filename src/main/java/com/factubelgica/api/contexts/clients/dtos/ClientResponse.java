package com.factubelgica.api.contexts.clients.dtos;

import com.factubelgica.api.contexts.clients.models.Client;

public record ClientResponse(
    Integer clientId,
    String vatNumber,
    Boolean active,
    Integer infoId,
    String legalName,
    String email,
    String address,
    String phone
) {
  public static ClientResponse fromClient(Client client) {
    return new ClientResponse(
        client.getClientId(),
        client.getVatNumber(),
        client.getActive(),
        client.getInfoId(),
        client.getLegalName(),
        client.getEmail(),
        client.getAddress(),
        client.getPhone()
    );
  }
}
