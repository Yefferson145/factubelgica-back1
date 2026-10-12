package com.factubelgica.api.contexts.catalog.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductSchema, Integer> {

  @Query(value = "SELECT * FROM products WHERE product_id > :lastId AND active = true ORDER BY product_id ASC LIMIT :limit", nativeQuery = true)
  List<ProductSchema> findProductsPaginated(@Param("lastId") Integer lastId, @Param("limit") int limit);
}