-- Crée (ou réinitialise) un administrateur de test.
-- Usage : psql ... -v admin_email=admin@asr.local -v admin_password=MotDePasse -f dev/seed-admin.sql
-- À lancer après le premier démarrage de l'API (Flyway crée les tables).
CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO asr.profile (created_at, last_modified_at, email, password, company, role, enabled)
VALUES (now(), now(), :'admin_email', '{bcrypt}' || crypt(:'admin_password', gen_salt('bf', 10)), 'ASR', 'ADMIN', true)
ON CONFLICT (email) DO UPDATE
    SET password = EXCLUDED.password, role = 'ADMIN', enabled = true, last_modified_at = now();
