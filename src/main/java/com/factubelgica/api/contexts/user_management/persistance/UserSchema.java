package com.factubelgica.api.contexts.user_management.persistance;

import com.factubelgica.api.contexts.user_management.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserSchema {
  @Id
  @Column(name = "user_id")
  private UUID id;

  @Column(nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(name = "pwd", nullable = false)
  private String password;

  private boolean active;

  @Column(nullable = false)
  private String role;

  @Column(insertable = false, updatable = false)
  private LocalDateTime created;

  public static UserSchema fromUser(User user) {
    UserSchema entity = new UserSchema();
    entity.setId(user.getUserId());
    entity.setUsername(user.getUserName());
    entity.setEmail(user.getEmail());
    entity.setPassword(user.getPassword());
    entity.setActive(user.getActive());
    entity.setRole(user.getRole().name().toLowerCase());

    return entity;
  }

  public User toUser() {
    return new User(
      this.id,
      this.username,
      this.email,
      this.password,
      this.active,
      User.Roles.valueOf(this.role.toUpperCase())
    );
  }
}
