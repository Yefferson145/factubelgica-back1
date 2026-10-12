package com.factubelgica.api.contexts.clients.persistance;

import com.factubelgica.api.contexts.clients.models.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients_mutable")
@Getter
@Setter
@NoArgsConstructor
public class ClientMutableSchema {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "info_id")
  private Integer infoId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id", nullable = false)
  private ClientSchema client;

  @Column(name = "legal_name", nullable = false)
  private String legalName;

  private String email;
  private String address;
  private String phone;

  @Column(insertable = false, updatable = false)
  private LocalDateTime created;

  public Client toClient() {
    return new Client(
        this.client.getId(),
        this.client.getVatNumber(),
        this.client.isActive(),
        this.infoId,
        this.legalName,
        this.email,
        this.address,
        this.phone
    );
  }
}
