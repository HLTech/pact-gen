echo "decrypting secring.gpg..."
openssl aes-256-cbc -K $encrypted_99d2b8555460_key -iv $encrypted_99d2b8555460_iv -in $TRAVIS_BUILD_DIR/travis/secring.gpg.enc -out $TRAVIS_BUILD_DIR/travis/secring.gpg -d

echo "uploadArchives..."
./gradlew uploadArchives -PnexusUsername=$SONATYPE_USER -PnexusPassword=$SONATYPE_PASSWORD -Psigning.secretKeyRingFile=$TRAVIS_BUILD_DIR/travis/secring.gpg

echo "closeAndPromoteRepository..."
./gradlew closeAndReleaseRepository --info
