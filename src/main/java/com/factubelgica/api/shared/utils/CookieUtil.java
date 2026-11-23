package com.factubelgica.api.shared.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieUtil {

  public static final String TOKEN_COOKIE_NAME = "factubelgica-token";
  public static final String TOKEN_COOKIE_PATH = "/";

  private static final Duration TOKEN_MAX_AGE = Duration.ofHours(2);

  private final boolean secure;
  private final String sameSite;

  public CookieUtil(
      @Value("${app.cookie.secure:false}") boolean secure,
      @Value("${app.cookie.same-site:Lax}") String sameSite) {
    String normalizedSameSite = normalizeSameSite(sameSite);
    // SameSite=None exige Secure=true en los navegadores modernos. Si se configura
    // None sin Secure, se fuerza Secure para evitar que el navegador rechace la
    // cookie en produccion.
    boolean effectiveSecure = secure || "None".equalsIgnoreCase(normalizedSameSite);
    this.secure = effectiveSecure;
    this.sameSite = normalizedSameSite;
  }

  private static String normalizeSameSite(String value) {
    if (value == null || value.isBlank()) {
      return "Lax";
    }
    String trimmed = value.trim();
    if (trimmed.equalsIgnoreCase("none")) {
      return "None";
    }
    if (trimmed.equalsIgnoreCase("lax")) {
      return "Lax";
    }
    if (trimmed.equalsIgnoreCase("strict")) {
      return "Strict";
    }
    return trimmed;
  }

  public ResponseCookie createTokenCookie(String token) {
    /*
     * En local (http://localhost:4200 -> http://localhost:8080) la cookie NO puede
     * llevar Secure=true ni SameSite=None, porque el navegador la rechaza al no
     * viajar por HTTPS. Por defecto se usa Secure=false y SameSite=Lax.
     *
     * En producción (frontend y backend en dominios distintos) se configura:
     *   COOKIE_SECURE=true
     *   COOKIE_SAME_SITE=None
     *
     * Las variables de Render se leen en application.yaml como:
     *   app.cookie.secure=${COOKIE_SECURE:false}
     *   app.cookie.same-site=${COOKIE_SAME_SITE:Lax}
     */
    return ResponseCookie.from(TOKEN_COOKIE_NAME, token)
        .path(TOKEN_COOKIE_PATH)
        .maxAge(TOKEN_MAX_AGE)
        .httpOnly(true)
        .secure(secure)
        .sameSite(sameSite)
        .build();
  }
}
