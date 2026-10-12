package com.factubelgica.api.shared.core;

import java.util.UUID;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import org.springframework.stereotype.Service;


@Service
public class UUIDv7 {
    NoArgGenerator generator = Generators.timeBasedEpochGenerator();

    public UUID generate() {
      return generator.generate();
    }
}
