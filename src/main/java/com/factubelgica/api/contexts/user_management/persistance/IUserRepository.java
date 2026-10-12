package com.factubelgica.api.contexts.user_management.persistance;

import com.factubelgica.api.contexts.user_management.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {
  public Optional<User> save(User user);
  public Optional<User> findByEmail(String email);
  public Optional<List<User>> getUsersPublicInfo(UUID lastID, int limit);
}
