package com.factubelgica.api.contexts.billing.persistance;

import com.factubelgica.api.contexts.billing.models.InvoiceItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceItemSchema {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Integer itemId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "invoice_id", nullable = false)
  private InvoiceSchema invoice;

  @Column(name = "product_id", nullable = false)
  private Integer productId;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal quantity;

  @Column(nullable = false)
  private String description;

  @Column(name = "net_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal netPrice;

  public static InvoiceItemSchema fromDomainModel(InvoiceItem item, InvoiceSchema invoiceSchema) {
    InvoiceItemSchema entity = new InvoiceItemSchema();
    entity.setInvoice(invoiceSchema);
    entity.setProductId(item.getProductId());
    entity.setQuantity(item.getQuantity());
    entity.setDescription(item.getDescription());
    entity.setNetPrice(item.getNetPrice());
    return entity;
  }

  public InvoiceItem toDomainModel() {
    return new InvoiceItem(
        this.itemId,
        this.productId,
        this.quantity,
        this.description,
        this.netPrice
    );
  }
}
