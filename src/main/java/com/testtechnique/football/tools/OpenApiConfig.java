// java
package com.testtechnique.football.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:football-team}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
    // Server entry (useful for local development)
    Server local = new Server();
    local.setUrl("http://localhost:" + serverPort);
    local.setDescription("Local server");

    // Security: JWT bearer
    SecurityScheme bearer = new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");

    SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

    return new OpenAPI()
        .components(new Components().addSecuritySchemes("bearerAuth", bearer))
        .addSecurityItem(securityRequirement)
        .addServersItem(local)
        .info(new Info()
            .title(appName + " API")
            .version(appVersion)
            .description("OpenAPI documentation for the " + appName + " application.")
            .contact(new Contact().name("Dev Team").email("joeladjidan@gmail.com"))
            .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
        );
    }
}