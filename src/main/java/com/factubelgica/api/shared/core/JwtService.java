package com.factubelgica.api.shared.core;

import com.factubelgica.api.shared.errors.UserUnauthorizedException;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

  private static final Logger log = LoggerFactory.getLogger(JwtService.class);
  private static final long EXPIRATION_TIME_MS = 2 * 60 * 60 * 1000;

  private final SecretKey key;

  public JwtService(Dotenv dotenv) {
    String secret = dotenv.get("JWT_SECRET_KEY", System.getenv("JWT_SECRET_KEY"));

    if (secret == null || secret.isBlank()) {
      throw new IllegalStateException(
          "JWT_SECRET_KEY is not configured. Define it in .env or as an environment variable");
    }

    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
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
