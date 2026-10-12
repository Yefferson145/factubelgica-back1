package com.factubelgica.api.contexts.user_management.dtos;

import com.factubelgica.api.contexts.user_management.models.User;

public record UserLoginResponse(String username, String email, String role) {
  public static UserLoginResponse fromUser(User user) {
    return new UserLoginResponse(user.getUserName(), user.getEmail(), user.getRole().name());
  }
}
