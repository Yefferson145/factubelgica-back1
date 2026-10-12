package com.factubelgica.api.contexts.catalog.persistance;

import com.factubelgica.api.contexts.catalog.models.Product;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {
  Optional<Product> save(Product product);
  Optional<Product> findById(Integer productId);
  Optional<List<Product>> getProductsPaginated(Integer lastId, int limit);
}