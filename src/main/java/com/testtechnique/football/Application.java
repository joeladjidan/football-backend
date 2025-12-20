/**
 * Point d’entrée Spring Boot de l’application Football.
 * <p>
 * @author Joël ADJIDAN
 * @since 2025-12-11
 */

package com.testtechnique.football;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
