-- V1 : Schéma initial pour équipes et joueurs
CREATE TABLE IF NOT EXISTS teams (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL UNIQUE,
    acronym VARCHAR(10) NOT NULL,
    budget DECIMAL(19,2) NOT NULL
    );

CREATE TABLE IF NOT EXISTS players (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
    position VARCHAR(64) NOT NULL,
    team_id BIGINT,
    CONSTRAINT fk_player_team FOREIGN KEY (team_id)
    REFERENCES teams(id) ON DELETE CASCADE
    );

-- Index utiles pour tri
CREATE INDEX idx_team_name ON teams(name);
CREATE INDEX idx_team_acronym ON teams(acronym);
CREATE INDEX idx_team_budget ON teams(budget);

-- ====================================
-- Seed data (idempotent)
-- ====================================

-- Liste étendue des clubs français (exemples) avec acronymes et budgets approximatifs
INSERT INTO teams (name, acronym, budget)
SELECT 'Paris Saint-Germain', 'PSG', 800000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Paris Saint-Germain');

INSERT INTO teams (name, acronym, budget)
SELECT 'Olympique de Marseille', 'OM', 220000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Olympique de Marseille');

INSERT INTO teams (name, acronym, budget)
SELECT 'Olympique Lyonnais', 'OL', 180000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Olympique Lyonnais');

INSERT INTO teams (name, acronym, budget)
SELECT 'AS Monaco', 'ASM', 250000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'AS Monaco');

INSERT INTO teams (name, acronym, budget)
SELECT 'LOSC Lille', 'LOSC', 120000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'LOSC Lille');

INSERT INTO teams (name, acronym, budget)
SELECT 'OGC Nice', 'OGC', 90000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'OGC Nice');

INSERT INTO teams (name, acronym, budget)
SELECT 'Stade Rennais FC', 'SRFC', 90000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Stade Rennais FC');

INSERT INTO teams (name, acronym, budget)
SELECT 'FC Nantes', 'FCN', 70000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'FC Nantes');

INSERT INTO teams (name, acronym, budget)
SELECT 'Girondins de Bordeaux', 'FCGB', 60000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Girondins de Bordeaux');

INSERT INTO teams (name, acronym, budget)
SELECT 'Stade Brestois 29', 'SB29', 45000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Stade Brestois 29');

INSERT INTO teams (name, acronym, budget)
SELECT 'Montpellier HSC', 'MHSC', 50000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Montpellier HSC');

INSERT INTO teams (name, acronym, budget)
SELECT 'Stade de Reims', 'SDR', 40000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Stade de Reims');

INSERT INTO teams (name, acronym, budget)
SELECT 'RC Lens', 'RCL', 110000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'RC Lens');

INSERT INTO teams (name, acronym, budget)
SELECT 'FC Lorient', 'FCL', 35000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'FC Lorient');

INSERT INTO teams (name, acronym, budget)
SELECT 'Toulouse FC', 'TFC', 30000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Toulouse FC');

INSERT INTO teams (name, acronym, budget)
SELECT 'Clermont Foot 63', 'CF63', 20000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Clermont Foot 63');

INSERT INTO teams (name, acronym, budget)
SELECT 'AC Ajaccio', 'ACA', 18000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'AC Ajaccio');

INSERT INTO teams (name, acronym, budget)
SELECT 'AJ Auxerre', 'AJA', 16000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'AJ Auxerre');

-- Example smaller clubs
INSERT INTO teams (name, acronym, budget)
SELECT 'Dijon FCO', 'DFCO', 8000000.00
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Dijon FCO');

-- Seeds: players (one or two exemples par équipe). position values use short codes or full names; V4 migration will allow longer strings.
-- Paris Saint-Germain
INSERT INTO players (name, position, team_id)
SELECT 'Kylian Mbappé', 'Attaquant', t.id FROM teams t WHERE t.name = 'Paris Saint-Germain' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Kylian Mbappé' AND p.team_id = t.id);
INSERT INTO players (name, position, team_id)
SELECT 'Lionel Messi', 'Attaquant', t.id FROM teams t WHERE t.name = 'Paris Saint-Germain' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Lionel Messi' AND p.team_id = t.id);

-- Olympique de Marseille
INSERT INTO players (name, position, team_id)
SELECT 'Dimitri Payet', 'Milieu', t.id FROM teams t WHERE t.name = 'Olympique de Marseille' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Dimitri Payet' AND p.team_id = t.id);

-- Olympique Lyonnais
INSERT INTO players (name, position, team_id)
SELECT 'Alexandre Lacazette', 'Attaquant', t.id FROM teams t WHERE t.name = 'Olympique Lyonnais' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Alexandre Lacazette' AND p.team_id = t.id);

-- AS Monaco
INSERT INTO players (name, position, team_id)
SELECT 'Wissam Ben Yedder', 'Attaquant', t.id FROM teams t WHERE t.name = 'AS Monaco' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Wissam Ben Yedder' AND p.team_id = t.id);

-- LOSC Lille
INSERT INTO players (name, position, team_id)
SELECT 'Jonathan David', 'Attaquant', t.id FROM teams t WHERE t.name = 'LOSC Lille' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Jonathan David' AND p.team_id = t.id);

-- OGC Nice
INSERT INTO players (name, position, team_id)
SELECT 'Khephren Thuram', 'Milieu', t.id FROM teams t WHERE t.name = 'OGC Nice' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Khephren Thuram' AND p.team_id = t.id);

-- Stade Rennais
INSERT INTO players (name, position, team_id)
SELECT 'Martin Terrier', 'Attaquant', t.id FROM teams t WHERE t.name = 'Stade Rennais FC' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Martin Terrier' AND p.team_id = t.id);

-- FC Nantes
INSERT INTO players (name, position, team_id)
SELECT 'Ludovic Blas', 'Milieu', t.id FROM teams t WHERE t.name = 'FC Nantes' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Ludovic Blas' AND p.team_id = t.id);

-- Girondins de Bordeaux
INSERT INTO players (name, position, team_id)
SELECT 'Hatem Ben Arfa', 'Milieu', t.id FROM teams t WHERE t.name = 'Girondins de Bordeaux' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Hatem Ben Arfa' AND p.team_id = t.id);

-- Stade Brestois 29
INSERT INTO players (name, position, team_id)
SELECT 'Steve Mounié', 'Attaquant', t.id FROM teams t WHERE t.name = 'Stade Brestois 29' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Steve Mounié' AND p.team_id = t.id);

-- Montpellier HSC
INSERT INTO players (name, position, team_id)
SELECT 'Andy Delort', 'Attaquant', t.id FROM teams t WHERE t.name = 'Montpellier HSC' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Andy Delort' AND p.team_id = t.id);

-- Stade de Reims
INSERT INTO players (name, position, team_id)
SELECT 'Boulaye Dia', 'Attaquant', t.id FROM teams t WHERE t.name = 'Stade de Reims' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Boulaye Dia' AND p.team_id = t.id);

-- RC Lens
INSERT INTO players (name, position, team_id)
SELECT 'Seko Fofana', 'Milieu', t.id FROM teams t WHERE t.name = 'RC Lens' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Seko Fofana' AND p.team_id = t.id);

-- FC Lorient
INSERT INTO players (name, position, team_id)
SELECT 'Terem Moffi', 'Attaquant', t.id FROM teams t WHERE t.name = 'FC Lorient' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Terem Moffi' AND p.team_id = t.id);

-- Toulouse FC
INSERT INTO players (name, position, team_id)
SELECT 'Bafodé Diakité', 'Défenseur', t.id FROM teams t WHERE t.name = 'Toulouse FC' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Bafodé Diakité' AND p.team_id = t.id);

-- Clermont Foot 63
INSERT INTO players (name, position, team_id)
SELECT 'Mickaël Alphonse', 'Défenseur', t.id FROM teams t WHERE t.name = 'Clermont Foot 63' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Mickaël Alphonse' AND p.team_id = t.id);

-- AC Ajaccio
INSERT INTO players (name, position, team_id)
SELECT 'Seko Fofana', 'Milieu', t.id FROM teams t WHERE t.name = 'AC Ajaccio' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Seko Fofana' AND p.team_id = t.id);

-- AJ Auxerre
INSERT INTO players (name, position, team_id)
SELECT 'Romain Faivre', 'Milieu', t.id FROM teams t WHERE t.name = 'AJ Auxerre' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Romain Faivre' AND p.team_id = t.id);

-- Dijon FCO
INSERT INTO players (name, position, team_id)
SELECT 'Julien Toudic', 'Attaquant', t.id FROM teams t WHERE t.name = 'Dijon FCO' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Julien Toudic' AND p.team_id = t.id);

-- Keep existing OGC Nice seed if present (idempotent)
INSERT INTO players (name, position, team_id)
SELECT 'Jean Dupont', 'Attaquant', t.id
FROM teams t
WHERE t.name = 'OGC Nice'
  AND NOT EXISTS (
    SELECT 1 FROM players p WHERE p.name = 'Jean Dupont' AND p.team_id = t.id
);

-- RC Strasbourg
INSERT INTO players (name, position, team_id)
SELECT 'Habib Diallo', 'Attaquant', t.id FROM teams t WHERE t.name = 'RC Strasbourg Alsace' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Habib Diallo' AND p.team_id = t.id);
INSERT INTO players (name, position, team_id)
SELECT 'Waly Ngom', 'Milieu', t.id FROM teams t WHERE t.name = 'RC Strasbourg Alsace' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Waly Ngom' AND p.team_id = t.id);

-- FC Metz
INSERT INTO players (name, position, team_id)
SELECT 'Ibrahima Niane', 'Attaquant', t.id FROM teams t WHERE t.name = 'FC Metz' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Ibrahima Niane' AND p.team_id = t.id);
INSERT INTO players (name, position, team_id)
SELECT 'Habib Mouhammed', 'Défenseur', t.id FROM teams t WHERE t.name = 'FC Metz' AND NOT EXISTS (SELECT 1 FROM players p WHERE p.name = 'Habib Mouhammed' AND p.team_id = t.id);

-- (you can extend the list with additional clubs/players as needed)

-- End of seed data
