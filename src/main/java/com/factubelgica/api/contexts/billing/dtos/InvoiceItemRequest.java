package com.factubelgica.api.contexts.billing.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record InvoiceItemRequest(
    @NotNull(message = "Product ID is required")
    Integer productId,

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    BigDecimal quantity
) {
}
