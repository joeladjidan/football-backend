package com.testtechnique.football.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Value("${spring.application.name:football-team}")
    private String applicationName;

    /**
     * Configure common tags on the primary MeterRegistry by injecting it directly.
     * Returning an Object bean just to trigger configuration during startup.
     */
    @Bean
    public Object configureMeterRegistry(MeterRegistry registry) {
        registry.config().commonTags("application", applicationName);
        return new Object();
    }

    // Small helper to deny some meters if needed later
    @Bean
    public MeterFilter meterFilter() {
        return MeterFilter.denyNameStartsWith("jvm.info");
    }

}
