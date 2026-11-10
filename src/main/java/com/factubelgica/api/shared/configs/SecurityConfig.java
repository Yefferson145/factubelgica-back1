package com.factubelgica.api.shared.configs;

import com.factubelgica.api.shared.core.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

  private final JwtAuthenticationFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Los preflight de CORS deben pasar siempre sin autenticacion.
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(
                "/auth/login",
                "/auth/register",
                "/auth/logout",
                "/error",
                "/api-docs/**",
                "/api-spec/**",
                "/swagger-ui/**",
                "/v3/api-docs/**"
            ).permitAll()
            /*
             * El filtro inyecta ROLE_ADMIN, ADMIN y admin (y equivalentes para USER),
             * por lo que cualquiera de estas variantes satisface la regla.
             */
            .requestMatchers("/a-role/**").hasAnyAuthority(
                "ROLE_ADMIN", "ADMIN", "admin"
            )
            // Un ADMIN tambien puede consumir /u-role/** (productos, clientes, etc.).
            .requestMatchers("/u-role/**").hasAnyAuthority(
                "ROLE_ADMIN", "ADMIN", "admin",
                "ROLE_USER", "USER", "user"
            )
            .anyRequest().authenticated()
        )
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) -> {
              log.warn("Unauthenticated request -> {} {}", request.getMethod(), request.getRequestURI());
              writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED,
                  "UNAUTHORIZED", "Authentication required");
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              log.warn("Access denied -> {} {} | authorities={}",
                  request.getMethod(),
                  request.getRequestURI(),
                  request.getUserPrincipal());
              writeJsonError(response, HttpServletResponse.SC_FORBIDDEN,
                  "FORBIDDEN", "You do not have permission to access this resource");
            })
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // localhost / 127.0.0.1 en cualquier puerto (Angular usa 4200) + produccion.
    config.setAllowedOriginPatterns(List.of(
        "http://localhost:*",
        "http://127.0.0.1:*",
        "https://factubelgica.vercel.app"
    ));
    config.setAllowedMethods(List.of(
        "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"
    ));
    // Con allowCredentials=true, "*" hace que Spring refleje los headers pedidos.
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Set-Cookie", "Authorization"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  private void writeJsonError(
      HttpServletResponse response,
      int status,
      String error,
      String message) throws java.io.IOException {
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(
        "{\"error\":\"" + error + "\",\"message\":\"" + message + "\"}");
  }
}
