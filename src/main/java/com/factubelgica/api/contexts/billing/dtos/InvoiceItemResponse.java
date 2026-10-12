package com.factubelgica.api.contexts.billing.dtos;

import com.factubelgica.api.contexts.billing.models.InvoiceItem;

import java.math.BigDecimal;

public record InvoiceItemResponse(
    Integer itemId,
    Integer productId,
    BigDecimal quantity,
    String description,
    BigDecimal netPrice,
    BigDecimal lineTotal   // quantity * netPrice
) {
  public static InvoiceItemResponse fromDomainModel(InvoiceItem item) {
    BigDecimal lineTotal = item.getNetPrice().multiply(item.getQuantity());
    return new InvoiceItemResponse(
        item.getItemId(),
        item.getProductId(),
        item.getQuantity(),
        item.getDescription(),
        item.getNetPrice(),
        lineTotal
    );
  }
}
