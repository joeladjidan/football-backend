package com.testtechnique.football.auth;

import com.testtechnique.football.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that handles user authentication.
 * Provides an endpoint to log in and obtain a JWT token.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtService jwtService;

    /**
     * Constructor to inject required dependencies.
     *
     * @param authenticationConfiguration Authentication configuration used to obtain the AuthenticationManager.
     * @param jwtService Service responsible for generating and handling JWT tokens.
     */
    public AuthController(AuthenticationConfiguration authenticationConfiguration, JwtService jwtService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtService = jwtService;
    }

    /**
     * POST endpoint to authenticate a user. If credentials are valid, returns a JWT token.
     *
     * @param request Request object containing user credentials (username and password).
     * @return HTTP response containing a JWT token on success or an error response on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        logger.info("Tentative de connexion d'un utilisateur");
        try {
            // Récupérer l'AuthenticationManager au moment de l'appel pour éviter les problèmes d'initialisation
            final AuthenticationManager authManager;
            try {
                authManager = authenticationConfiguration.getAuthenticationManager();
            } catch (Exception e) {
                logger.error("Impossible d'obtenir AuthenticationManager", e);
                return ResponseEntity.status(500).body(new ErrorResponse("Authentication service unavailable"));
            }
            // Authentifie l'utilisateur avec les identifiants fournis.
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username, request.password)
            );
            // Génère un token JWT pour l'utilisateur authentifié.
            String token = jwtService.generateToken((UserDetails) auth.getPrincipal());
            logger.info("Authentification réussie pour un utilisateur");
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (AuthenticationException ex) {
            // Capture et log les erreurs d'authentification.
            logger.warn("Échec d'authentification lors d'une tentative de connexion: {}", ex.getMessage());
            logger.debug("Détail de l'exception d'authentification", ex);
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials"));
        }
    }

    /**
     * Internal class representing a login request.
     * Contains the fields required for authentication.
     */
    public static class LoginRequest {
        public String username; // Username
        public String password; // Password
    }

    /**
     * Internal class representing a response that contains a JWT token.
     */
    public static class TokenResponse {
        public String token; // JWT token

        /**
         * Constructor to initialize the token.
         *
         * @param t JWT token
         */
        public TokenResponse(String t) {
            this.token = t;
        }
    }

    /**
     * Internal class representing an error response.
     */
    public static class ErrorResponse {
        public String error; // Error message

        /**
         * Constructor to initialize the error message.
         *
         * @param error Error message
         */
        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}
