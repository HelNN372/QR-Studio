#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
: "${ANDROID_HOME:?Set ANDROID_HOME to an Android SDK containing platform 35 and build-tools 35.0.0}"
BT="$ANDROID_HOME/build-tools/35.0.0"
PLATFORM="$ANDROID_HOME/platforms/android-35/android.jar"
mkdir -p build/classes build/dex build/compiled libs
if [ ! -f libs/zxing-core-3.5.3.jar ]; then
  curl -fL --retry 3 https://repo.maven.apache.org/maven2/com/google/zxing/core/3.5.3/core-3.5.3.jar -o libs/zxing-core-3.5.3.jar
fi
printf '%s  %s\n' '8d8064c1636fdaef7189dd9055c7d59950a8940a12f2293956446ec3c109fd82' 'libs/zxing-core-3.5.3.jar' | sha256sum -c -
javac -source 8 -target 8 -encoding UTF-8 -classpath "$PLATFORM:libs/zxing-core-3.5.3.jar" -d build/classes src/ru/colpak/qrstudio/*.java
jar cf build/classes.jar -C build/classes .
"$BT/d8" --release --min-api 26 --lib "$PLATFORM" --output build/dex build/classes.jar libs/zxing-core-3.5.3.jar
"$BT/aapt2" compile --dir res -o build/compiled
"$BT/aapt2" link -I "$PLATFORM" --manifest AndroidManifest.xml -o build/unsigned.apk build/compiled/*.flat
(cd build/dex && zip -q -0 ../unsigned.apk classes.dex)
"$BT/zipalign" -f 4 build/unsigned.apk build/aligned.apk
if [ ! -f signing.p12 ]; then keytool -genkeypair -keystore signing.p12 -storepass android -keypass android -alias qrstudio -keyalg RSA -keysize 3072 -validity 10000 -dname "CN=QR Studio"; fi
"$BT/apksigner" sign --ks signing.p12 --ks-pass pass:android --out QR-Studio.apk build/aligned.apk
"$BT/apksigner" verify --verbose QR-Studio.apk
