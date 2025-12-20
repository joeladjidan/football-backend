-- Increase players.position length to 64 to avoid truncation when using full position names
ALTER TABLE players MODIFY COLUMN position VARCHAR(64) NOT NULL;

