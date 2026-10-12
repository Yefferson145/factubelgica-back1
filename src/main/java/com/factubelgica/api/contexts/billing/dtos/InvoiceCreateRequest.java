package com.factubelgica.api.contexts.billing.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoiceCreateRequest(
    @NotNull(message = "Client ID is required")
    Integer clientId,

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    LocalDate dueDate,

    /**
     * Optional VAT rate override. If null, the service will apply the default 21% (0.21).
     */
    @Positive(message = "VAT rate must be positive")
    BigDecimal vatRate,

    @NotEmpty(message = "Invoice must have at least one item")
    @Valid
    List<InvoiceItemRequest> items
) {
}
