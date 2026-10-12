package com.factubelgica.api.contexts.clients.persistance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
public class ClientSchema {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "client_id")
  private Integer id;

  @Column(name = "vat_number", nullable = false, unique = true)
  private String vatNumber;

  @Column(nullable = false)
  private boolean active;

  @Column(insertable = false, updatable = false)
  private LocalDateTime created;
}
