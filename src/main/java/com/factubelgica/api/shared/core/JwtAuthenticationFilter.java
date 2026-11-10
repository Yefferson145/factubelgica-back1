package com.factubelgica.api.shared.core;

import com.factubelgica.api.shared.errors.UserUnauthorizedException;
import com.factubelgica.api.shared.utils.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtService jwtService;
  private final JwtRepository jwtRepository;
  private final HandlerExceptionResolver resolver;

  public JwtAuthenticationFilter(
      JwtService jwtService,
      JwtRepository jwtRepository,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.jwtService = jwtService;
    this.jwtRepository = jwtRepository;
    this.resolver = resolver;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String path = request.getServletPath();

    /*
     * Los preflight (OPTIONS) nunca llevan cookies ni Authorization. Deben pasar
     * siempre al CorsFilter / SecurityFilterChain para que respondan 200.
     */
    if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || isPublicPath(path)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String token = extractToken(request);

      if (token == null || token.isBlank()) {
        throw new UserUnauthorizedException(
            "JWT token not found in cookie '" + CookieUtil.TOKEN_COOKIE_NAME + "' or Authorization header");
      }

      UUID userId = jwtService.validateTokenAndGetSubject(token);

      String role = jwtRepository.findUserRoleById(userId)
          .filter(value -> !value.isBlank())
          .orElseThrow(() -> new UserUnauthorizedException(
              "User unauthorized: no role found for user " + userId));

      List<GrantedAuthority> authorities = buildAuthorities(role);

      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
          userId,
          null,
          authorities
      );
      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(auth);

      log.debug("JWT OK -> user={}, role='{}', authorities={}", userId, role, authorities);

      filterChain.doFilter(request, response);

    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      log.warn("JWT authentication failed for {} {}: {}", request.getMethod(), path, e.getMessage());
      resolver.resolveException(request, response, null, e);
    }
  }

  /**
   * Busca primero en la cookie exacta definida en CookieUtil y, como fallback,
   * en el header Authorization: Bearer &lt;token&gt;.
   */
  private String extractToken(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (CookieUtil.TOKEN_COOKIE_NAME.equals(cookie.getName())) {
          String cookieValue = cookie.getValue();
          if (cookieValue != null && !cookieValue.isBlank()) {
            return cookieValue;
          }
        }
      }
    }

    String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorization != null
        && authorization.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
      String bearerToken = authorization.substring(BEARER_PREFIX.length()).trim();
      if (!bearerToken.isEmpty()) {
        return bearerToken;
      }
    }

    return null;
  }

  /**
   * Genera autoridades compatibles con cualquier variante del rol guardado en BD
   * (admin, ADMIN, ROLE_ADMIN, user, USER, ROLE_USER...). Asi evitamos mismatches
   * entre hasRole/hasAuthority y el valor real de la columna users.role.
   */
  private List<GrantedAuthority> buildAuthorities(String role) {
    String normalized = role.trim().toUpperCase(Locale.ROOT);
    if (normalized.startsWith("ROLE_")) {
      normalized = normalized.substring("ROLE_".length());
    }

    Set<String> authorityNames = new LinkedHashSet<>();
    authorityNames.add("ROLE_" + normalized);
    authorityNames.add(normalized);
    authorityNames.add(normalized.toLowerCase(Locale.ROOT));

    List<GrantedAuthority> authorities = new ArrayList<>(authorityNames.size());
    for (String authorityName : authorityNames) {
      authorities.add(new SimpleGrantedAuthority(authorityName));
    }
    return authorities;
  }

  private boolean isPublicPath(String path) {
    return path.startsWith("/api-docs")
        || path.startsWith("/api-spec")
        || path.startsWith("/swagger-ui")
        || path.startsWith("/v3/api-docs")
        || path.equals("/auth/login")
        || path.equals("/auth/register")
        || path.equals("/auth/logout")
        || path.equals("/error");
  }
}
