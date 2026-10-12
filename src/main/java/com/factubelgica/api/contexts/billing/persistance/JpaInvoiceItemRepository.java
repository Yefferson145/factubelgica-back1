package com.factubelgica.api.contexts.billing.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInvoiceItemRepository extends JpaRepository<InvoiceItemSchema, Integer> {
}
