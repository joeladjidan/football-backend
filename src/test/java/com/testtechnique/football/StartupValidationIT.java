package com.testtechnique.football;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.WebApplicationType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartupValidationIT {

    @Test
    void failsToStartInProdWhenJwtSecretMissing() {
        Map<String, Object> props = new HashMap<>();
        props.put("spring.profiles.active", "prod");
        // Use an in-memory database to avoid external dependencies during the test
        props.put("spring.datasource.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false");
        props.put("spring.datasource.driverClassName", "org.h2.Driver");
        props.put("spring.datasource.username", "sa");
        props.put("spring.datasource.password", "");
        props.put("spring.main.web-application-type", "none");

        Exception thrown = assertThrows(Exception.class, () -> {
            try (ConfigurableApplicationContext ctx = new SpringApplicationBuilder(Application.class)
                    .properties(props)
                    .web(WebApplicationType.NONE)
                    .run()) {
                // If startup succeeds unexpectedly, context will be closed by try-with-resources
            }
        });

        // The thrown exception or its cause should mention the missing secret or be an IllegalStateException
        String message = thrown.getMessage() != null ? thrown.getMessage() : "";
        Throwable cause = thrown.getCause();
        String causeMessage = (cause != null && cause.getMessage() != null) ? cause.getMessage() : "";
        assertTrue(message.contains("APP_JWT_SECRET") || causeMessage.contains("APP_JWT_SECRET") || thrown instanceof IllegalStateException || (cause != null && cause instanceof IllegalStateException));
    }
}

