package com.testtechnique.football.config;

// ...existing code...

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

@Configuration
public class FlywayConfig {

    @Value("${app.flyway.auto-repair:false}")
    private boolean autoRepair;

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            if (autoRepair) {
                try {
                    // Attempt repair to update checksums from modified migrations (dev only)
                    flyway.repair();
                } catch (Exception e) {
                    // Log and continue to fail later if migration still invalid
                    System.err.println("Flyway repair failed: " + e.getMessage());
                }
            }
            // proceed with normal migrate (will validate and migrate)
            flyway.migrate();
        };
    }
}

