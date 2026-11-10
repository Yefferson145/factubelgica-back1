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
    this.secure = secure;
    this.sameSite = (sameSite == null || sameSite.isBlank()) ? "Lax" : sameSite;
  }

  public ResponseCookie createTokenCookie(String token) {
    /*
     * En local (http://localhost:4200 -> http://localhost:8080) la cookie NO puede
     * llevar Secure=true ni SameSite=None, porque el navegador la rechaza al no
     * viajar por HTTPS. Por defecto se usa Secure=false y SameSite=Lax.
     *
     * En producción se puede sobrescribir con:
     *   APP_COOKIE_SECURE=true
     *   APP_COOKIE_SAME_SITE=None
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
