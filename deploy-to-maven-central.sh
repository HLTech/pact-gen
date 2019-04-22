echo "decrypting secring.gpg..."
openssl aes-256-cbc -K $encrypted_12c8071d2874_key -iv $encrypted_12c8071d2874_iv -in $TRAVIS_BUILD_DIR/secring.gpg.enc -out $TRAVIS_BUILD_DIR/secring.gpg -d

echo "publish archives..."
./gradlew publish -Psigning.keyId=$signingKeyId -Psigning.password=$secretKeyPassword -Psigning.secretKeyRingFile=$TRAVIS_BUILD_DIR/secring.gpg --info --stacktrace

echo "closeAndPromoteRepository..."
./gradlew closeAndReleaseRepository --info
