package com.factubelgica.api.contexts.billing.dtos;

import jakarta.validation.constraints.NotBlank;

public record InvoiceStatusUpdateRequest(
    @NotBlank(message = "Status is required")
    String status
) {
}
