package com.factubelgica.api.contexts.user_management.services;

import com.factubelgica.api.contexts.user_management.dtos.UserItemResponse;
import com.factubelgica.api.contexts.user_management.errors.UsersNotFound;
import com.factubelgica.api.contexts.user_management.models.User;
import com.factubelgica.api.contexts.user_management.persistance.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListUsers {
  private final UserRepository repository;

  public List<User> execute(UUID lastID, int limit) {
    return repository
        .getUsersPublicInfo(lastID, limit)
        .orElseThrow(() -> new UsersNotFound("There are no more users"));
  }
}
