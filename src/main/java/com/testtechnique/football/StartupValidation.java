package com.testtechnique.football;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupValidation implements ApplicationRunner {

    private final Environment env;

    public StartupValidation(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] activeProfiles = env.getActiveProfiles();
        boolean isProd = false;
        for (String p : activeProfiles) {
            if ("prod".equalsIgnoreCase(p)) {
                isProd = true;
                break;
            }
        }

        if (isProd) {
            String secret = env.getProperty("APP_JWT_SECRET");
            if (secret == null || secret.trim().isEmpty()) {
                // fallback to application property name
                secret = env.getProperty("app.jwt.secret");
            }

            if (secret == null || secret.trim().isEmpty()) {
                throw new IllegalStateException("APP_JWT_SECRET is required when running with 'prod' profile. Aborting startup.");
            }
        }
    }
}

