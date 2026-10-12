package com.factubelgica.api.shared.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Slf4j {
  public static final Logger logger = LoggerFactory.getLogger(Slf4j.class);
}