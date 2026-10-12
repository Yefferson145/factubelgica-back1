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
    String sql = "SELECT role FROM users WHERE user_id = ?";

    try {
      String role = jdbcTemplate.queryForObject(sql, String.class, userId);
      return Optional.ofNullable(role);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    } catch (DataAccessException dae) {
      Slf4j.logger.error("Database error while fetching role for user: {}", userId, dae);
      return Optional.empty();
    }
  }
}
