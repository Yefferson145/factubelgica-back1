package com.factubelgica.api.contexts.catalog.persistance;

import com.factubelgica.api.contexts.catalog.models.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class ProductSchema {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Vinculado al SERIAL de Postgres
  @Column(name = "product_id")
  private Integer id;

  @Column(nullable = false)
  private String description;

  @Column(name = "net_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal netPrice;

  @Column(nullable = false)
  private boolean active;

  @Column(insertable = false, updatable = false)
  private LocalDateTime created;

  public static ProductSchema fromProduct(Product product) {
    ProductSchema entity = new ProductSchema();
    // Si es una creación (id = 0), dejamos que Hibernate maneje la generación automática por el SERIAL
    if (product.getProductId() != null && product.getProductId() > 0) {
      entity.setId(product.getProductId());
    }
    entity.setDescription(product.getDescription());
    entity.setNetPrice(product.getNetPrice());
    entity.setActive(product.getActive());

    return entity;
  }

  public Product toProduct() {
    return new Product(
        this.id,
        this.description,
        this.netPrice,
        this.active
    );
  }
}