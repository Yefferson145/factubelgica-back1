package com.factubelgica.api.contexts.user_management.services;

import com.factubelgica.api.contexts.user_management.dtos.UserLoginRequest;
import com.factubelgica.api.contexts.user_management.models.User;
import com.factubelgica.api.contexts.user_management.persistance.IUserRepository;
import com.factubelgica.api.contexts.user_management.utils.PasswordHasher;
import com.factubelgica.api.shared.errors.UserUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogin {
  private final IUserRepository repository;
  private final PasswordHasher hasher;

  public User execute(UserLoginRequest req) {
    User userStored = repository
        .findByEmail(req.email())
        .orElseThrow(() -> new UserUnauthorizedException("User not exists"));

    if (!hasher.compare(req.password(), userStored.getPassword())) {
      throw new UserUnauthorizedException("Incorrect password");
    }
    return userStored;
  }
}
