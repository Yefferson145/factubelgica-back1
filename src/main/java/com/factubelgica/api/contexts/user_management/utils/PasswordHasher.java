package com.factubelgica.api.contexts.user_management.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public String hash(String plainPassword) {
    return encoder.encode(plainPassword);
  }

  public Boolean compare(String plainPassword, String hashedPassword) {
    return encoder.matches(plainPassword, hashedPassword);
  }
}
