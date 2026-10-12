package com.factubelgica.api.contexts.dashboard.persistance;

import com.factubelgica.api.contexts.dashboard.models.ProductInfo;
import com.factubelgica.api.contexts.dashboard.models.RawInvoiceItem;
import com.factubelgica.api.shared.utils.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DashboardRepository implements IDashboardRepository {

  private final JpaDashboardRepository jpaDashboardRepository;

  @Override
  public Optional<List<RawInvoiceItem>> findRawInvoiceItemsByYear(int year) {
    try {
      List<Object[]> rows = jpaDashboardRepository.findRawInvoiceItemsByYear(year);
      List<RawInvoiceItem> items = new ArrayList<>();

      for (Object[] row : rows) {
        Integer productId = (Integer) row[0];
        String description = (String) row[1];
        BigDecimal productNetPrice = row[2] instanceof BigDecimal ? (BigDecimal) row[2] : new BigDecimal(row[2].toString());

        LocalDate issueDate;
        Object dateObj = row[3];
        if (dateObj instanceof java.sql.Date) {
          issueDate = ((java.sql.Date) dateObj).toLocalDate();
        } else if (dateObj instanceof java.time.LocalDate) {
          issueDate = (LocalDate) dateObj;
        } else {
          issueDate = LocalDate.parse(dateObj.toString());
        }

        BigDecimal quantity = row[4] instanceof BigDecimal ? (BigDecimal) row[4] : new BigDecimal(row[4].toString());
        BigDecimal itemNetPrice = row[5] instanceof BigDecimal ? (BigDecimal) row[5] : new BigDecimal(row[5].toString());
        BigDecimal vatRate = row[6] instanceof BigDecimal ? (BigDecimal) row[6] : new BigDecimal(row[6].toString());

        items.add(new RawInvoiceItem(
            productId,
            description,
            productNetPrice,
            issueDate,
            quantity,
            itemNetPrice,
            vatRate
        ));
      }

      return Optional.of(items);
    } catch (Exception e) {
      Slf4j.logger.warn("Error fetching raw invoice items for year: {}", year, e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<ProductInfo> findProductById(int productId) {
    try {
      List<Object[]> rows = jpaDashboardRepository.findProductById(productId);
      if (rows.isEmpty()) return Optional.empty();

      Object[] row = rows.get(0);
      Integer id = (Integer) row[0];
      String description = (String) row[1];
      BigDecimal netPrice = row[2] instanceof BigDecimal ? (BigDecimal) row[2] : new BigDecimal(row[2].toString());

      return Optional.of(new ProductInfo(id, description, netPrice));
    } catch (Exception e) {
      Slf4j.logger.warn("Error fetching product by id: {}", productId, e);
      return Optional.empty();
    }
  }
}
