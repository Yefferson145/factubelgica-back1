package com.factubelgica.api.contexts.catalog.dtos;

import com.factubelgica.api.contexts.catalog.models.Product;

import java.math.BigDecimal;

public record ProductItemResponse(Integer productId, String description, BigDecimal netPrice, Boolean active) {
  public static ProductItemResponse fromProduct(Product product) {
    return new ProductItemResponse(
        product.getProductId(),
        product.getDescription(),
        product.getNetPrice(),
        product.getActive()
    );
  }
}