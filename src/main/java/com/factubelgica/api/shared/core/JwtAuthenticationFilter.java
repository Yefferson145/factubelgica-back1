package com.factubelgica.api.shared.core;

import com.factubelgica.api.shared.errors.UserUnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

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

    if (path.startsWith("/api-docs") ||
        path.startsWith("/api-spec") ||
        path.startsWith("/swagger-ui") ||
        path.startsWith("/v3/api-docs") ||
        path.equals("/auth/login") ||
        path.startsWith("/u-role/invoices/")
    ) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String token = null;
      if (request.getCookies() != null) {
        token = Arrays.stream(request.getCookies())
            .filter(c -> "factubelgica-token".equals(c.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);
      }

      if (token == null) {
        throw new UserUnauthorizedException("Session cookies not found");
      }

      UUID userId = jwtService.validateTokenAndGetSubject(token);

      String role = jwtRepository.findUserRoleById(userId)
          .orElseThrow(() -> new UserUnauthorizedException("User unauthorized"));

      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
          userId,
          null,
          List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
      );

      SecurityContextHolder.getContext().setAuthentication(auth);

      filterChain.doFilter(request, response);

    } catch (Exception e) {
      resolver.resolveException(request, response, null, e);
    }
  }
}