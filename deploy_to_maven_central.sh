#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

function cleanup {
    echo "🧹 Cleanup..."
    rm -f gradle.properties secring.gpg
}

trap cleanup SIGINT SIGTERM ERR EXIT

echo "🚀 Preparing to deploy..."

echo "🔑 Decrypting files..."
gpg --quiet --batch --yes --debug-all --decrypt --passphrase="${GPG_SECRET}" \
    --output secring.gpg secring.gpg.enc

gpg --quiet --batch --yes --debug-all --decrypt --passphrase="${GPG_SECRET}" \
    --output gradle.properties gradle.properties.enc

gpg --fast-import --no-tty --batch --yes secring.gpg

echo "📦 Publishing..."

./gradlew currentVersion publishToSonatype closeAndReleaseSonatypeStagingRepository

echo "✅ Done!"
