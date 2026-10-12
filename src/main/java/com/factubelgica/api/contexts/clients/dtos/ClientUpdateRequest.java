package com.factubelgica.api.contexts.clients.dtos;

import com.factubelgica.api.contexts.clients.models.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientUpdateRequest(
    @NotBlank(message = "Legal name is required")
    @Size(max = 255, message = "Legal name is too long")
    String legalName,

    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email is too long")
    String email,

    @Size(max = 500, message = "Address is too long")
    String address,

    @Size(max = 50, message = "Phone is too long")
    String phone,

    @NotNull(message = "Active status is required")
    Boolean active
) {
  public Client toClient(Integer clientId, String vatNumber) {
    return new Client(
        clientId,
        vatNumber,
        this.active,
        null,
        this.legalName.strip(),
        this.email != null && !this.email.isBlank() ? this.email.strip() : null,
        this.address != null && !this.address.isBlank() ? this.address.strip() : null,
        this.phone != null && !this.phone.isBlank() ? this.phone.strip() : null
    );
  }
}
