package com.factubelgica.api.contexts.user_management.dtos;

import com.factubelgica.api.contexts.user_management.models.User;

import java.util.UUID;

public record UserItemResponse(
    UUID userId,
    String username,
    String email,
    String role,
    boolean active
) {
  public static UserItemResponse fromUser(User user) {
    return new UserItemResponse(
        user.getUserId(),
        user.getUserName(),
        user.getEmail(),
        user.getRole().toString(),
        user.getActive()
    );
  }
}
