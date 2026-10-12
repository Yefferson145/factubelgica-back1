package com.factubelgica.api.shared.core;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
  private final SecretKey key;
  private final long expirationTime = 2 * 60 * 60 * 1000;

  public JwtService(Dotenv dotenv) {
    String secret = dotenv.get("JWT_SECRET_KEY", System.getenv("JWT_SECRET_KEY"));
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(UUID userId) {
    return Jwts.builder()
        .subject(userId.toString())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(key)
        .compact();
  }

  public UUID validateTokenAndGetSubject(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    return UUID.fromString(claims.getSubject());
  }
}