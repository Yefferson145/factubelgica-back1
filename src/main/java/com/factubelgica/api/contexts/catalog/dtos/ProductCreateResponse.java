package com.factubelgica.api.contexts.catalog.dtos;

import com.factubelgica.api.contexts.catalog.models.Product;

import java.math.BigDecimal;

public record ProductCreateResponse(Integer productId, String description, BigDecimal netPrice) {
  public static ProductCreateResponse fromProduct(Product product) {
    return new ProductCreateResponse(
        product.getProductId(),
        product.getDescription(),
        product.getNetPrice()
    );
  }
}