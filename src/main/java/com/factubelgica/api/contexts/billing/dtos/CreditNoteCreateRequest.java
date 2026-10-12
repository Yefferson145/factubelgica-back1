package com.factubelgica.api.contexts.billing.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreditNoteCreateRequest(
    @NotNull(message = "Invoice ID is required")
    UUID invoiceId,

    @NotBlank(message = "Reason is required")
    String reason
) {
}
