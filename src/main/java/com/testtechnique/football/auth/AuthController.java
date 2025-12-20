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
 * Contrôleur REST pour la gestion de l'authentification des utilisateurs.
 * Fournit un endpoint pour se connecter et obtenir un token JWT.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtService jwtService;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param authenticationConfiguration Configuration d'authentification permettant d'obtenir l'AuthenticationManager.
     * @param jwtService Service pour la génération et la gestion des tokens JWT.
     */
    public AuthController(AuthenticationConfiguration authenticationConfiguration, JwtService jwtService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint POST pour authentifier un utilisateur.
     * Si les identifiants sont valides, retourne un token JWT.
     *
     * @param request Objet contenant les identifiants de l'utilisateur (username et password).
     * @return Réponse HTTP contenant un token JWT en cas de succès ou une erreur en cas d'échec.
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
     * Classe interne représentant une requête de connexion.
     * Contient les champs nécessaires pour l'authentification.
     */
    public static class LoginRequest {
        public String username; // Nom d'utilisateur.
        public String password; // Mot de passe.
    }

    /**
     * Classe interne représentant une réponse contenant un token JWT.
     */
    public static class TokenResponse {
        public String token; // Token JWT.

        /**
         * Constructeur pour initialiser le token.
         *
         * @param t Token JWT.
         */
        public TokenResponse(String t) {
            this.token = t;
        }
    }

    /**
     * Classe interne représentant une réponse d'erreur.
     */
    public static class ErrorResponse {
        public String error; // Message d'erreur.

        /**
         * Constructeur pour initialiser le message d'erreur.
         *
         * @param error Message d'erreur.
         */
        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}
