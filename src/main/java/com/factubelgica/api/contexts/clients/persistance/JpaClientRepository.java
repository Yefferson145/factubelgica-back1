package com.factubelgica.api.contexts.clients.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JpaClientRepository extends JpaRepository<ClientSchema, Integer> {
  Optional<ClientSchema> findByVatNumber(String vatNumber);
}
