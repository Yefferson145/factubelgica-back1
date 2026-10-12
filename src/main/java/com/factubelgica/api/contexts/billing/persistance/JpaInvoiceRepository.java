package com.factubelgica.api.contexts.billing.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaInvoiceRepository extends JpaRepository<InvoiceSchema, UUID> {

  /**
   * Keyset pagination for a specific user (USER role access control).
   * Uses UUID lexicographic ordering — mirrors the pattern in JpaUserRepository.
   */
  @Query(value = """
      SELECT * FROM invoices
      WHERE user_id = :userId
        AND (:lastId IS NULL OR invoice_id > CAST(:lastId AS uuid))
      ORDER BY invoice_id ASC
      LIMIT :limit
      """, nativeQuery = true)
  List<InvoiceSchema> findInvoicesByUserIdPaginated(
      @Param("userId") UUID userId,
      @Param("lastId") String lastId,
      @Param("limit") int limit
  );

  /**
   * Keyset pagination for all invoices (ADMIN access).
   */
  @Query(value = """
      SELECT * FROM invoices
      WHERE (:lastId IS NULL OR invoice_id > CAST(:lastId AS uuid))
      ORDER BY invoice_id ASC
      LIMIT :limit
      """, nativeQuery = true)
  List<InvoiceSchema> findAllInvoicesPaginated(
      @Param("lastId") String lastId,
      @Param("limit") int limit
  );

  /**
   * Update invoice status (and optionally payment_date).
   */
  @Modifying
  @Query(value = """
      UPDATE invoices
      SET status = :status, payment_date = :paymentDate
      WHERE invoice_id = CAST(:invoiceId AS uuid)
      """, nativeQuery = true)
  void updateStatus(
      @Param("invoiceId") String invoiceId,
      @Param("status") String status,
      @Param("paymentDate") LocalDate paymentDate
  );
}
