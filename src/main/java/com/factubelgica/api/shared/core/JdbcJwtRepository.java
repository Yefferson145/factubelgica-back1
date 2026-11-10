package com.factubelgica.api.shared.core;

import com.factubelgica.api.shared.utils.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcJwtRepository implements JwtRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public Optional<String> findUserRoleById(UUID userId) {
    // La columna users.role guarda 'admin'/'user' (en minusculas), pero se
    // normaliza en Java dentro del filtro para aceptar cualquier variante.
    String sql = "SELECT role FROM users WHERE user_id = ?";

    try {
      String role = jdbcTemplate.queryForObject(sql, String.class, userId);

      if (role == null || role.isBlank()) {
        Slf4j.logger.warn("User {} exists but has an empty role", userId);
        return Optional.empty();
      }

      String normalizedRole = role.trim();
      Slf4j.logger.debug("Role '{}' found for user {}", normalizedRole, userId);
      return Optional.of(normalizedRole);
    } catch (EmptyResultDataAccessException e) {
      Slf4j.logger.warn("No user found with id {}", userId);
      return Optional.empty();
    } catch (DataAccessException dae) {
      Slf4j.logger.error("Database error while fetching role for user: {}", userId, dae);
      return Optional.empty();
    }
  }
}
