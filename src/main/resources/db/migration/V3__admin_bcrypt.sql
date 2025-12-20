-- V3 : Création d'un administrateur avec mot de passe BCrypt
-- Mot de passe en clair (à titre d'exemple) : Admin#2025!
-- Hash stocké au format Spring Security Delegating: {bcrypt}<hash>

-- Upsert admin si absent (portable)
INSERT INTO users (username, password, roles)
SELECT 'admin', '{bcrypt}$2b$10$ovGWw1GdC/RY6.M8Hf5euORvEmgP0pc2le8Is5hQy2jdT0RXzjXr6', 'ROLE_USER,ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Insert user 'joeladjidan' if absent (portable)
INSERT INTO users (username, password, roles)
SELECT 'joeladjidan', '{bcrypt}$2a$10$Fk4GzscxoChhqPEZOGo/Je1mcHFhgOCbDBen03WqqFMDcsU6qDGUq', 'ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'joeladjidan');
