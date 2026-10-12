package com.factubelgica.api.contexts.billing.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaCreditNoteRepository extends JpaRepository<CreditNoteSchema, UUID> {

  @Query(value = """
      SELECT * FROM credit_notes
      WHERE (:lastId IS NULL OR note_id > CAST(:lastId AS uuid))
      ORDER BY note_id ASC
      LIMIT :limit
      """, nativeQuery = true)
  List<CreditNoteSchema> findCreditNotesPaginated(
      @Param("lastId") String lastId,
      @Param("limit") int limit
  );
}
