package com.factubelgica.api.shared.configs;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Carga el archivo .env de desarrollo local como una fuente de propiedades de
 * Spring. Se registra con la MENOR prioridad: cualquier variable de entorno real
 * (por ejemplo las de Render) o propiedad del sistema prevalece sobre .env.
 *
 * En produccion el archivo no existe y este post-procesador no hace nada.
 */
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

  private static final Log log = LogFactory.getLog(DotenvEnvironmentPostProcessor.class);
  private static final String PROPERTY_SOURCE_NAME = "dotenv";

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
    if (environment.getPropertySources().contains(PROPERTY_SOURCE_NAME)) {
      return;
    }

    try {
      Dotenv dotenv = Dotenv.configure()
          .ignoreIfMissing()
          .ignoreIfMalformed()
          .load();

      Map<String, Object> values = new LinkedHashMap<>();
      for (DotenvEntry entry : dotenv.entries()) {
        if (entry.getKey() != null && entry.getValue() != null) {
          values.put(entry.getKey(), entry.getValue());
        }
      }

      if (!values.isEmpty()) {
        environment.getPropertySources()
            .addLast(new MapPropertySource(PROPERTY_SOURCE_NAME, values));
        log.info("Loaded " + values.size() + " properties from .env (lowest priority)");
      }
    } catch (Exception e) {
      // Un .env mal formado no debe impedir que la aplicacion arranque con las
      // variables de entorno del sistema.
      log.warn("Could not load .env file; system environment variables will be used", e);
    }
  }
}
