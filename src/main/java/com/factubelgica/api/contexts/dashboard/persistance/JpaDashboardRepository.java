package com.factubelgica.api.contexts.dashboard.persistance;

import com.factubelgica.api.contexts.billing.persistance.InvoiceSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaDashboardRepository extends JpaRepository<InvoiceSchema, UUID> {

  @Query(value = """
      SELECT
        ii.product_id,
        p.description,
        p.net_price AS product_net_price,
        i.issue_date,
        ii.quantity,
        ii.net_price AS item_net_price,
        i.vat_rate
      FROM invoice_items ii
      JOIN invoices i ON ii.invoice_id = i.invoice_id
      JOIN products p ON ii.product_id = p.product_id
      WHERE EXTRACT(YEAR FROM i.issue_date) = :year
        AND i.status IN ('Pagado', 'Enviado')
      """, nativeQuery = true)
  List<Object[]> findRawInvoiceItemsByYear(@Param("year") int year);

  @Query(value = """
      SELECT product_id, description, net_price
      FROM products
      WHERE product_id = :id
        AND active = true
      """, nativeQuery = true)
  List<Object[]> findProductById(@Param("id") int productId);
}
