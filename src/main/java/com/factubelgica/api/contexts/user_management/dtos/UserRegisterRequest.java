package com.factubelgica.api.contexts.user_management.dtos;

import com.factubelgica.api.contexts.user_management.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserRegisterRequest(
    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username too long")
    String username,
    @Email(message = "User email format is invalid")
    String email,
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password too weak")
    String password,
    @NotBlank(message = "Role is required")
    String role
) {
    public User toUser(UUID userId, String hashedPassword) {
      return new User(
        userId,
        this.username.strip(),
        this.email.strip(),
        hashedPassword,
  true,
        parseRole(role.strip())
      );
    }

    private User.Roles parseRole(String roleName) {
      try {
        return User.Roles.valueOf(roleName.toUpperCase());
      } catch (IllegalArgumentException e) {
        return User.Roles.USER;
      }
    }
}
