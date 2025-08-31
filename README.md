# Tsumiyoku Worldbuilding Site

Wiki + annonces (gouvernementales, scientifiques) avec rôles Discord (édition/publication réservées).

## Démarrer en local

1. MariaDB ou MySQL en route, créer une base `worldsite` (ou laisser `createDatabaseIfNotExist=true` faire le taf).
2. Exporter les secrets Discord :
   ```bash
   setx DISCORD_CLIENT_ID "xxxxxxxx"
   setx DISCORD_CLIENT_SECRET "xxxxxxxx"

mvn spring-boot:run puis ouvrir http://localhost:8082

## Rôles et sécurité

Les rôles d'édition sont ROLE_EDITOR, ROLE_GOV, ROLE_SCI, ROLE_ADMIN.

Par défaut, si aucun rôle n'est détecté via l'ID Discord/guild, l'utilisateur a ROLE_VIEWER (lecture seule).

Pour mapper des rôles de votre serveur Discord vers les rôles de l'app, stockez vos correspondances dans role_mapping (à
compléter si vous implémentez l'appel à l'API Discord Guild).

Note : L'exemple se contente d'accepter un champ roles côté OAuth attributes s'il existe. Pour une intégration complète,
ajoutez un DiscordGuildService qui, après login, interroge l'API Discord avec le token pour lister les rôles dans une ou
plusieurs guilds autorisées puis mappez vers les rôles app.

## Points clés

Wiki : /wiki/{slug} (vue), /editor/wiki/{slug} (édition), POST /editor/wiki/save

Annonces : /announcements/{type} avec type ∈ {gov,sci}. Création via /editor/announcements/new/{type}

Markdown rendu côté serveur via CommonMark.

Versionnage : WikiRevision historise chaque sauvegarde.

## Intégration avec le site gouvernemental

Vous pouvez réutiliser la même application Discord et le même callback si les domaines sont communs.

Sinon ajustez spring.security.oauth2.client.registration.discord.redirect-uri.

Si vous avez déjà un module DiscordOAuth2UserService dans le projet gouv, déplacez-le dans un module partagé Maven (ou
publiez un jar interne) et importez-le ici.

## GitHub Actions & déploiement

Ajoutez des secrets : DISCORD_CLIENT_ID, DISCORD_CLIENT_SECRET, DB_URL, DB_USER, DB_PASS.

Exemple de variables JVM :

SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD.

Flyway migre le schéma au démarrage.

## À faire (pistes)

Éditeur Markdown plus riche (toolbar) + prévisualisation live.

Modération / workflow (Brouillon → Relecture → Publication).

Upload de pièces jointes avec stockage S3/MinIO.

Recherche plein texte (PostgreSQL + to_tsvector ou Elasticsearch).

Webhook Discord pour publier automatiquement un message lors d'une nouvelle annonce.