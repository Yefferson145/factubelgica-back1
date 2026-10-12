package com.factubelgica.api.contexts.user_management.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class User {
  @NonNull
  private final UUID userId;
  @NonNull
  private final String userName;
  @NonNull
  private final String email;
  @NonNull
  private final String password;
  @NonNull
  private final Boolean active;
  @NonNull
  private final Roles role;

  public enum Roles {
    USER,
    ADMIN
  }
}
