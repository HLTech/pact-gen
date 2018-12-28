echo "decrypting secring.gpg..."
openssl aes-256-cbc -K $encrypted_99d2b8555460_key -iv $encrypted_99d2b8555460_iv -in $TRAVIS_BUILD_DIR/travis/secring.gpg.enc -out $TRAVIS_BUILD_DIR/travis/secring.gpg -d

echo "uploadArchives..."
../gradlew uploadArchives -Psigning.keyId=$signingKeyId -Psigning.password=$secretKeyPassword -Psigning.secretKeyRingFile=$TRAVIS_BUILD_DIR/travis/secring.gpg --info --stacktrace

echo "closeAndPromoteRepository..."
../gradlew closeAndReleaseRepository --info
