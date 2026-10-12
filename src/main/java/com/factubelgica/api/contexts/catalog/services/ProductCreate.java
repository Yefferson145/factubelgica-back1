package com.factubelgica.api.contexts.catalog.services;

import com.factubelgica.api.contexts.catalog.dtos.ProductCreateRequest;
import com.factubelgica.api.contexts.catalog.errors.ErrorCreatingProduct;
import com.factubelgica.api.contexts.catalog.models.Product;
import com.factubelgica.api.contexts.catalog.persistance.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCreate {
  private final IProductRepository repository;

  public Product execute(ProductCreateRequest req) {
    // Al ser un SERIAL, pasamos temporalmente 0, el Schema se encargará de ignorarlo para el insert
    Product productToSave = req.toProduct(0);

    return repository
        .save(productToSave)
        .orElseThrow(() -> new ErrorCreatingProduct("Product cannot be created"));
  }
}