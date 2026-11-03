#!/usr/bin/env bash
# Génère des clés JWT jetables pour le développement local dans .env.dev.
# Ne jamais réutiliser les clés de production.
set -euo pipefail
cd "$(dirname "$0")/.."

if [ -f .env.dev ]; then
  echo ".env.dev existe déjà : supprimez-le pour régénérer les clés." >&2
  exit 1
fi

tmp="$(mktemp)"
trap 'rm -f "$tmp"' EXIT
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out "$tmp" 2>/dev/null

private_key="$(openssl pkcs8 -topk8 -nocrypt -in "$tmp" -outform DER | openssl base64 -A)"
public_key="$(openssl pkey -in "$tmp" -pubout -outform DER | openssl base64 -A)"

cat > .env.dev <<ENV
JWT_PRIVATE_KEY=${private_key}
JWT_PUBLIC_KEY=${public_key}
DATASOURCE_URL=jdbc:postgresql://localhost:5432/postgres
DATASOURCE_USERNAME=postgres
DATASOURCE_PASSWORD=test
ENV
echo ".env.dev créé (clés de test, hors Git)."
