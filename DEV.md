# Développement local

Faire tourner l'API, une base PostgreSQL de test et le front sur votre poste, sans toucher à la production.

## Prérequis
- Git
- Docker Desktop (base de données + API)
- Node.js 22 et pnpm (front) : `npm install -g pnpm`
- `openssl` (déjà présent sur Mac et Linux ; sous Windows, utiliser Git Bash ou WSL)

## 1. API et base de données
```bash
git clone https://github.com/mbetoulouse-hub/api-chiffrage-asr.git
cd api-chiffrage-asr
./dev/gen-jwt-keys.sh                       # crée .env.dev (clés jetables, hors Git)
docker compose -f docker-compose.dev.yml up --build
```
L'API écoute sur `http://localhost:7670`. Au premier démarrage, les tables sont créées automatiquement.

## 2. Premier utilisateur (administrateur)
Il n'existe aucun compte au départ et la création de profil exige d'être connecté : il faut donc en insérer un directement.
Dans un second terminal, API démarrée :
```bash
docker compose -f docker-compose.dev.yml exec -T db psql -U postgres \
  -v admin_email=admin@asr.local -v admin_password='ChoisissezUnMotDePasse' \
  < dev/seed-admin.sql
```
Vous pouvez ensuite créer d'autres utilisateurs depuis le panneau d'administration du front. Relancer la commande réinitialise le mot de passe de cet administrateur.

## 3. Front
```bash
git clone https://github.com/mbetoulouse-hub/front-chiffrage-asr.git
cd front-chiffrage-asr
echo "NEXT_PUBLIC_API_BASE_URL=http://localhost:7670/api/v1" > .env.local
pnpm install
pnpm dev
```
Ouvrez `http://localhost:3000` et connectez-vous avec l'administrateur créé à l'étape 2. Chaque modification du code s'affiche immédiatement.

L'adresse doit se terminer par `/api/v1` : le front y ajoute `/auth/login`, `/estimate/...`, etc.

## Sans Docker
- PostgreSQL 16 installé localement, mot de passe `test` pour l'utilisateur `postgres`.
- `./dev/gen-jwt-keys.sh`, puis `set -a; . ./.env.dev; set +a; ./gradlew bootRun` (Java 21).

## Bon à savoir
- `.env.dev` et `.env.local` ne doivent jamais être poussés sur Git (ils sont ignorés).
- N'utilisez jamais les clés ni la base de production en local.
- Remise à zéro de la base : `docker compose -f docker-compose.dev.yml down -v`.
- Le nom de l'utilisateur de test et son mot de passe sont ceux que vous choisissez à l'étape 2.
