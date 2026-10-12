package com.factubelgica.api.shared.utils;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
  public ResponseCookie createTokenCookie(String token) {
    return ResponseCookie.from("factubelgica-token", token)
        .path("/")
        .maxAge(2 * 60 * 60)
        .httpOnly(true)
        .secure(true)
        .sameSite("None")
        .build();
  }
}