package com.factubelgica.api.contexts.user_management.dtos;

import com.factubelgica.api.contexts.user_management.models.User;

public record UserRegisterResponse(String username, String email, String role) {
  public static UserRegisterResponse fromUser(User user) {
    return new UserRegisterResponse(user.getUserName(), user.getEmail(), user.getRole().name());
  }
}
