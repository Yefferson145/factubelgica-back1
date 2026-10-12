package com.factubelgica.api.contexts.clients.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

@RequiredArgsConstructor
@Getter
public class Client {
  @NonNull
  private final Integer clientId;
  @NonNull
  private final String vatNumber;
  @NonNull
  private final Boolean active;

  // Mutable details (latest snapshot)
  private final Integer infoId;
  private final String legalName;
  private final String email;
  private final String address;
  private final String phone;
}
