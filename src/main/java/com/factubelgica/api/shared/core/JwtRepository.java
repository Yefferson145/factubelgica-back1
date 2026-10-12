package com.factubelgica.api.shared.core;

import java.util.Optional;
import java.util.UUID;

public interface JwtRepository {
  Optional<String> findUserRoleById(UUID userId);
}
