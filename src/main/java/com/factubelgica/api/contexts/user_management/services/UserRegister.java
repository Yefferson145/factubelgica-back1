package com.factubelgica.api.contexts.user_management.services;

import com.factubelgica.api.contexts.user_management.dtos.UserRegisterRequest;
import com.factubelgica.api.contexts.user_management.errors.ErrorCreatingUser;
import com.factubelgica.api.contexts.user_management.errors.UserAlreadyExists;
import com.factubelgica.api.contexts.user_management.models.User;
import com.factubelgica.api.contexts.user_management.persistance.IUserRepository;
import com.factubelgica.api.contexts.user_management.utils.PasswordHasher;
import com.factubelgica.api.shared.core.UUIDv7;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRegister {
  private final IUserRepository repository;
  private final UUIDv7 v7;
  private final PasswordHasher hasher;

  public User execute(UserRegisterRequest req) {
    repository
        .findByEmail(req.email())
        .ifPresent(user -> {
          throw new UserAlreadyExists("Email " + req.email() + " is already used");
        });

    UUID userId = v7.generate();
    String hashedPassword = hasher.hash(req.password());
    User userToSave = req.toUser(userId, hashedPassword);

    return repository
        .save(userToSave)
        .orElseThrow(() -> new ErrorCreatingUser("User with email "+ req.email() +" cannot be created"));
  }
}
