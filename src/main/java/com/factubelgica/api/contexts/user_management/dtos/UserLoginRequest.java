package com.factubelgica.api.contexts.user_management.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(
    @Email(message = "User email format is invalid")
    String email,
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password too weak")
    String password
) {
}
