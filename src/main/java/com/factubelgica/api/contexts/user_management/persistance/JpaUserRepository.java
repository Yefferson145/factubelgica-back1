package com.factubelgica.api.contexts.user_management.persistance;

import com.factubelgica.api.contexts.user_management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserSchema, UUID> {
  @Query("SELECT u FROM UserSchema u WHERE u.email = :email AND u.active = true")
  Optional<UserSchema> findByEmail(@Param("email") String email);

  @Query(value = "SELECT * FROM users WHERE user_id > :lastId ORDER BY user_id ASC LIMIT :limit", nativeQuery = true)
  List<UserSchema> findUsersPaginated(@Param("lastId") UUID lastId, @Param("limit") int limit);
}
