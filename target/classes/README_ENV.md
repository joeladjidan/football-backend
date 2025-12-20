Variables d'environnement attendues pour l'application backend

Fichiers de configuration disponibles:
- `application.yml` (paramètres communs)
- `application-dev.yml` (profil de développement)
- `application-prod.yml` (profil de production)

Variables principales:
- SPRING_PROFILES_ACTIVE: dev | prod (par défaut: dev)
- SPRING_DATASOURCE_URL: JDBC URL vers la base de données
- SPRING_DATASOURCE_USERNAME: nom d'utilisateur DB
- SPRING_DATASOURCE_PASSWORD: mot de passe DB
- APP_JWT_SECRET: secret JWT (exigé pour la prod)
- APP_JWT_EXPIRATION_MS: durée de validité du token en ms (optionnel, défaut 3600000)

Exemples PowerShell pour démarrer via Docker Compose

- Dev (utilise les valeurs par défaut; attention: secret par défaut non recommandé):

```powershell
$env:SPRING_PROFILES_ACTIVE = "dev"; docker-compose up --build
```

- Prod (exemple en passant les secrets — préférez un gestionnaire de secrets en prod):

```powershell
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:SPRING_DATASOURCE_USERNAME = "prod_user"
$env:SPRING_DATASOURCE_PASSWORD = "prod_pass"
$env:APP_JWT_SECRET = "votre_secret_production"
docker-compose up --build -d
```

Remarques:
- Ne stockez pas `APP_JWT_SECRET` ni les mots de passe DB dans le dépôt.
- En production, utilisez un gestionnaire de secrets (Vault, Azure KeyVault, AWS Secrets Manager, etc.).
- Vous pouvez aussi fournir ces variables via un fichier `.env` ou Docker secrets plutôt que via variables d'environnement en clair.
