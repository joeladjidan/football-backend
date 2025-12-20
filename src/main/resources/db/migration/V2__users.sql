-- V2 : Table des utilisateurs pour authentification JWT
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    roles   VARCHAR(255) NOT NULL
);

-- Seed user de démo (utiliser des comptes réels en prod). Password format Spring Security Delegating: {noop}admin123
INSERT INTO users (username, password, roles)
SELECT 'admin', '{noop}admin123', 'ROLE_USER,ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');
