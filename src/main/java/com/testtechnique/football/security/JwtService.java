package com.testtechnique.football.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

/**
 * Service utilitaire pour la génération et la validation de tokens JWT (HS256).
 * <p>
 * @author Joël ADJIDAN
 * @since 2025-12-11
 */
@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private boolean verboseLogging = false;

    @Value("${app.jwt.secret:ZmFrZS1zZWNyZXQtY2hhbmdlLW1lLXBsZWFzZQ==}")
    private String secretBase64;

    @Value("${app.jwt.expiration-ms:3600000}") // 1h par défaut
    private long expirationMs;

    private final Environment env;

    public JwtService(Environment env) {
        this.env = env;
    }

    /**
     * Retourne une Key HMAC-SHA compatible (>= 256 bits). Si le secret configuré est
     * trop court (p.ex. valeur de dev), on dérive une clé 256-bit via SHA-256 sur
     * l'entrée fournie. Si la valeur n'est pas en base64, on l'utilise brute.
     */
    private Key getKey() {
        try {
            byte[] decoded;
            try {
                decoded = Decoders.BASE64.decode(secretBase64);
            } catch (Exception e) {
                // pas du Base64 ? on utilise les octets UTF-8 bruts
                decoded = secretBase64.getBytes(StandardCharsets.UTF_8);
                if (verboseLogging) {
                    log.debug("Le secret JWT n'est pas un Base64 valide — utilisation des octets UTF-8 bruts");
                }
            }

            if (decoded.length < 32) {
                // dérive une clé 256-bit via SHA-256
                try {
                    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                    byte[] keyBytes = sha256.digest(decoded);
                    log.warn("Le secret JWT fourni est trop court ({} bytes). Dérivation d'une clé 256-bit via SHA-256.", decoded.length);
                    return Keys.hmacShaKeyFor(keyBytes);
                } catch (NoSuchAlgorithmException ex) {
                    log.error("SHA-256 non disponible pour dériver la clé JWT; génération d'une clé aléatoire sûre en mémoire", ex);
                    return Keys.secretKeyFor(SignatureAlgorithm.HS256);
                }
            }

            return Keys.hmacShaKeyFor(decoded);
        } catch (Exception e) {
            log.error("Impossible de construire la clé de signature JWT à partir du secret configuré; génération d'une clé sûre par défaut", e);
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
    }

    @PostConstruct
    private void init() {
        String[] profiles = env.getActiveProfiles();
        verboseLogging = Arrays.asList(profiles).contains("dev") || Arrays.asList(profiles).contains("local");
        log.info("JwtService démarré, profils actifs = {}", Arrays.toString(profiles));
        if (verboseLogging) {
            log.debug("Logging JWT en mode VERBOSE (dev/local)");
            log.debug("Secret JWT (base64) = {}", secretBase64 == null ? "<null>" : (secretBase64.length() > 8 ? secretBase64.substring(0, 8) + "..." : secretBase64));
        }
    }

    /**
     * Génère un JWT signé HS256 pour l'utilisateur donné, avec les rôles en claim.
     * @param user principal pour lequel générer le token
     * @return token JWT compact
     */
    public String generateToken(UserDetails user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("roles", user.getAuthorities().stream().map(org.springframework.security.core.GrantedAuthority::getAuthority).toList())
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        if (verboseLogging) {
            log.debug("Token généré pour utilisateur={} expiresAt={} token={}", user.getUsername(), exp, token);
        } else {
            log.info("Token généré pour utilisateur={}", user.getUsername());
        }
        return token;
    }


    /**
     * Extrait le username (subject) depuis un token JWT.
     * @param token JWT
     * @return username
     */
    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getKey()).build()
                    .parseClaimsJws(token).getBody();
            String user = claims.getSubject();
            if (verboseLogging) {
                log.debug("Username extrait depuis le token : {} ; claims={}", user, claims);
            } else {
                log.debug("Username extrait depuis le token : {}", user);
            }
            return user;
        } catch (JwtException | IllegalArgumentException e) {
            if (verboseLogging) {
                log.debug("Impossible d'extraire le username depuis le token", e);
            } else {
                log.warn("Impossible d'extraire le username depuis le token : {}", e.getMessage());
            }
            throw e;
        }
    }

}
