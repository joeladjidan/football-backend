package com.testtechnique.football.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Configuration
public class MetricsConfig {

    @Value("${spring.application.name:football-team}")
    private String applicationName;

    @Value("${management.metrics.export.logging.enabled:false}")
    private boolean loggingEnabled;

    @Value("${management.metrics.export.logging.step:PT1M}")
    private String loggingStep;

    /**
     * Configure common tags on the primary MeterRegistry by injecting it directly.
     * Returning an Object bean just to trigger configuration during startup.
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    /**
     * Provide a LoggingMeterRegistry that periodically writes metrics to the application logs.
     * Enabled with property: management.metrics.export.logging.enabled=true
     */
    @Bean
    @ConditionalOnProperty(prefix = "management.metrics.export.logging", name = "enabled", havingValue = "true")
    @ConditionalOnClass(name = "io.micrometer.core.instrument.logging.LoggingMeterRegistry")
    public MeterRegistry loggingMeterRegistry() {
        try {
            Class<?> configInterface = Class.forName("io.micrometer.core.instrument.logging.LoggingRegistryConfig");
            Object configProxy = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{configInterface}, (proxy, method, args) -> null);

            Class<?> loggingClass = Class.forName("io.micrometer.core.instrument.logging.LoggingMeterRegistry");
            Class<?> clockClass = Class.forName("io.micrometer.core.instrument.Clock");
            Field systemField = clockClass.getField("SYSTEM");
            Object clockInstance = systemField.get(null);

            Constructor<?> ctor = loggingClass.getConstructor(configInterface, clockClass);
            Object lmr = ctor.newInstance(configProxy, clockInstance);
            Method start = loggingClass.getMethod("start");
            start.invoke(lmr);
            return (MeterRegistry) lmr;
        } catch (Exception e) {
            // Logging registry not available on classpath or creation failed — skip silently
            return null;
        }
    }

    // Small helper to deny some meters if needed later
    @Bean
    public MeterFilter meterFilter() {
        return MeterFilter.denyNameStartsWith("jvm.info");
    }

}
