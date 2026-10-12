package com.factubelgica.api.contexts.clients.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaClientMutableRepository extends JpaRepository<ClientMutableSchema, Integer> {

  @Query("SELECT cm FROM ClientMutableSchema cm JOIN FETCH cm.client c " +
         "WHERE c.id = :clientId AND cm.infoId = (" +
         "  SELECT MAX(cm2.infoId) FROM ClientMutableSchema cm2 WHERE cm2.client.id = c.id" +
         ")")
  Optional<ClientMutableSchema> findLatestByClientId(@Param("clientId") Integer clientId);

  @Query("SELECT cm FROM ClientMutableSchema cm JOIN FETCH cm.client c " +
         "WHERE c.vatNumber = :vatNumber AND cm.infoId = (" +
         "  SELECT MAX(cm2.infoId) FROM ClientMutableSchema cm2 WHERE cm2.client.id = c.id" +
         ")")
  Optional<ClientMutableSchema> findLatestByVatNumber(@Param("vatNumber") String vatNumber);

  @Query(value = "SELECT cm.* FROM clients_mutable cm " +
         "INNER JOIN clients c ON cm.client_id = c.client_id " +
         "WHERE c.client_id > :lastId AND c.active = true " +
         "AND cm.info_id = (SELECT MAX(cm2.info_id) FROM clients_mutable cm2 WHERE cm2.client_id = c.client_id) " +
         "ORDER BY c.client_id ASC LIMIT :limit", nativeQuery = true)
  List<ClientMutableSchema> findActiveClientsPaginated(@Param("lastId") Integer lastId, @Param("limit") int limit);
}
