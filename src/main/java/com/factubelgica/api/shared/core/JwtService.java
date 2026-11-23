package com.factubelgica.api.shared.core;

import com.factubelgica.api.shared.errors.UserUnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

  private static final Logger log = LoggerFactory.getLogger(JwtService.class);
  private static final long EXPIRATION_TIME_MS = 2 * 60 * 60 * 1000;

  /**
   * HS256 exige una clave de al menos 256 bits (32 bytes). Con una clave mas
   * larga, jjwt selecciona automaticamente HS384/HS512.
   */
  private static final int MIN_SECRET_BYTES = 32;

  private final SecretKey key;

  public JwtService(@Value("${app.jwt.secret:}") String secret) {
    if (secret == null || secret.isBlank()) {
      throw new IllegalStateException(
          "JWT_SECRET_KEY is not configured. Define it as an environment variable "
              + "(Render) or in .env for local development");
    }

    byte[] secretBytes = secret.trim().getBytes(StandardCharsets.UTF_8);
    if (secretBytes.length < MIN_SECRET_BYTES) {
      throw new IllegalStateException(
          "JWT_SECRET_KEY is too short: " + secretBytes.length
              + " bytes. Use at least " + MIN_SECRET_BYTES
              + " bytes (256 bits), for example a 48-byte random value encoded in Base64");
    }

    this.key = Keys.hmacShaKeyFor(secretBytes);
    // Nunca se registra el valor del secreto, solo su longitud en bits.
    log.info("JWT signing key initialized with {} bits of key material", secretBytes.length * 8);
  }

  public String generateToken(UUID userId) {
    return Jwts.builder()
        .subject(userId.toString())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
        .signWith(key)
        .compact();
  }

  public UUID validateTokenAndGetSubject(String token) {
    try {
      Claims claims = Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token)
          .getPayload();

      String subject = claims.getSubject();
      if (subject == null || subject.isBlank()) {
        throw new UserUnauthorizedException("JWT token does not contain a subject");
      }

      UUID userId = UUID.fromString(subject);
      log.debug("JWT valid for user {} (expires at {})", userId, claims.getExpiration());
      return userId;

    } catch (JwtException | IllegalArgumentException e) {
      // Token expirado, firma invalida, formato incorrecto, subject no UUID...
      throw new UserUnauthorizedException("Invalid or expired JWT token: " + e.getMessage());
    }
  }
}
