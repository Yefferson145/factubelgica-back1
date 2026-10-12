package com.factubelgica.api.contexts.clients.persistance;

import com.factubelgica.api.contexts.clients.models.Client;
import com.factubelgica.api.shared.utils.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientRepository implements IClientRepository {

  private final JpaClientRepository jpaClientRepository;
  private final JpaClientMutableRepository jpaClientMutableRepository;

  @Override
  @Transactional
  public Optional<Client> save(Client client) {
    try {
      ClientSchema clientSchema;
      if (client.getClientId() != null && client.getClientId() > 0) {
        clientSchema = jpaClientRepository.findById(client.getClientId())
            .orElseThrow(() -> new IllegalArgumentException("Client not found: " + client.getClientId()));
        clientSchema.setActive(client.getActive());
      } else {
        clientSchema = new ClientSchema();
        clientSchema.setVatNumber(client.getVatNumber());
        clientSchema.setActive(client.getActive());
      }
      clientSchema = jpaClientRepository.save(clientSchema);

      ClientMutableSchema mutableSchema = new ClientMutableSchema();
      mutableSchema.setClient(clientSchema);
      mutableSchema.setLegalName(client.getLegalName());
      mutableSchema.setEmail(client.getEmail());
      mutableSchema.setAddress(client.getAddress());
      mutableSchema.setPhone(client.getPhone());

      ClientMutableSchema savedMutable = jpaClientMutableRepository.save(mutableSchema);
      return Optional.of(savedMutable.toClient());
    } catch (Exception e) {
      Slf4j.logger.warn("Error saving client with VAT: {}", client.getVatNumber(), e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Client> findById(Integer clientId) {
    try {
      return jpaClientMutableRepository.findLatestByClientId(clientId)
          .map(ClientMutableSchema::toClient);
    } catch (Exception e) {
      Slf4j.logger.warn("Error finding client by ID: {}", clientId, e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Client> findByVatNumber(String vatNumber) {
    try {
      return jpaClientMutableRepository.findLatestByVatNumber(vatNumber)
          .map(ClientMutableSchema::toClient);
    } catch (Exception e) {
      Slf4j.logger.warn("Error finding client by VAT: {}", vatNumber, e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<Client>> getClientsPaginated(Integer lastId, int limit) {
    try {
      return Optional.of(
          jpaClientMutableRepository.findActiveClientsPaginated(lastId, limit)
              .stream()
              .map(ClientMutableSchema::toClient)
              .toList()
      );
    } catch (Exception e) {
      Slf4j.logger.warn("Error fetching clients paginated from lastId: {} with limit of {}", lastId, limit, e);
      return Optional.empty();
    }
  }
}
