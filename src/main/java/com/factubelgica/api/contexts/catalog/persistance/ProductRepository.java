package com.factubelgica.api.contexts.catalog.persistance;

import com.factubelgica.api.contexts.catalog.models.Product;
import com.factubelgica.api.shared.utils.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepository implements IProductRepository {

  private final JpaProductRepository jpaProductRepository;

  @Override
  public Optional<Product> save(Product product) {
    try {
      ProductSchema newProduct = ProductSchema.fromProduct(product);
      ProductSchema savedProduct = jpaProductRepository.save(newProduct);
      return Optional.of(savedProduct.toProduct()); // o savedProduct.toProduct() según corresponda
    } catch (Exception e) {
      Slf4j.logger.warn("Error saving product with description: {}", product.getDescription(), e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Product> findById(Integer productId) {
    try {
      return jpaProductRepository.findById(productId)
          .map(ProductSchema::toProduct);
    } catch (Exception e) {
      Slf4j.logger.warn("Error finding product by ID: {}", productId, e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<Product>> getProductsPaginated(Integer lastId, int limit) {
    try {
      return Optional.of(
          jpaProductRepository
              .findProductsPaginated(lastId, limit)
              .stream()
              .map(ProductSchema::toProduct)
              .toList()
      );
    } catch (Exception e) {
      Slf4j.logger.warn("Error fetching products paginated from lastId: {} with limit of {}", lastId, limit, e);
      return Optional.empty();
    }
  }
}