package com.factubelgica.api.contexts.dashboard.persistance;

import com.factubelgica.api.contexts.dashboard.models.ProductInfo;
import com.factubelgica.api.contexts.dashboard.models.RawInvoiceItem;

import java.util.List;
import java.util.Optional;

public interface IDashboardRepository {
  Optional<List<RawInvoiceItem>> findRawInvoiceItemsByYear(int year);
  Optional<ProductInfo> findProductById(int productId);
}
