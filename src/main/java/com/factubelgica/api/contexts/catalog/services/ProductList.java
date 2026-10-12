package com.factubelgica.api.contexts.catalog.services;

import com.factubelgica.api.contexts.catalog.errors.ProductsNotFound;
import com.factubelgica.api.contexts.catalog.models.Product;
import com.factubelgica.api.contexts.catalog.persistance.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductList {
  private final IProductRepository repository;

  public List<Product> execute(Integer lastId, int limit) {
    return repository
        .getProductsPaginated(lastId, limit)
        .filter(list -> !list.isEmpty())
        .orElseThrow(() -> new ProductsNotFound("No products found from lastId: " + lastId));
  }
}