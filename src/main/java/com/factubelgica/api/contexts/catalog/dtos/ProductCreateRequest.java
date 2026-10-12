package com.factubelgica.api.contexts.catalog.dtos;

import com.factubelgica.api.contexts.catalog.models.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductCreateRequest(
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description is too long")
    String description,

    @NotNull(message = "Net price is required")
    @Positive(message = "Net price must be greater than zero")
    BigDecimal netPrice
) {
  public Product toProduct(Integer productId) {
    return new Product(
        productId,
        this.description.strip(),
        this.netPrice,
        true
    );
  }
}