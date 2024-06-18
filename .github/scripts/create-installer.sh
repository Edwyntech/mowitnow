#!/bin/bash

# expected environment variables:
# OS - The OS this script is running on (ubuntu-latest, windows-latest, macos-latest)
# JAVA_VERSION - Java version in maven format (17,21,...)
# JAVA_HOME - JDK home
echo "OS: ${OS}"
echo "JAVA_VERSION: ${JAVA_VERSION}"
echo "JAVA_HOME: ${JAVA_HOME}"
echo "Running from ${PWD}"

function mvn_evaluate() {
    result=$(mvn -f ui-desktop/pom.xml validate help:evaluate -Dexpression="$1" -q -DforceStdout)
    echo "$result"
}

ROOT_DIR=$(mvn_evaluate project.rootDirectory)
TARGET_DIRECTORY=$(mvn_evaluate project.build.directory)
PROJECT_VERSION=$(mvn_evaluate project.version)
MAIN_JAR="$(mvn_evaluate project.build.finalName).jar"
MAIN_CLASS=$(mvn_evaluate main.class)
MAIN_CLASSPATH="$(mvn_evaluate project.build.outputDirectory)/$(echo "${MAIN_CLASS}" | sed -r 's/\./\//g').class"

ICON="${ROOT_DIR}/.github/assets/icons/mowitnow.png"
if [ "${OS}" = 'ubuntu-latest' ]; then
  ICON="${ROOT_DIR}/.github/assets/icons/mowitnow.png"
elif [ "${OS}" = 'macos-latest' ]; then
  ICON="${ROOT_DIR}/.github/assets/icons/mowitnow.icns"
elif [ "${OS}" = 'windows-latest' ]; then
  ICON="${ROOT_DIR}/.github/assets/icons/mowitnow.ico"
fi

echo "ROOT_DIR: ${ROOT_DIR}"
echo "TARGET_DIRECTORY: ${TARGET_DIRECTORY}"
echo "PROJECT_VERSION: ${PROJECT_VERSION}"
echo "MAIN_JAR: ${MAIN_JAR}"
echo "MAIN_CLASS: ${MAIN_CLASS}"
echo "MAIN_CLASSPATH: ${MAIN_CLASSPATH}"
echo "ICON: ${ICON}"

function list_modules() {
  result=$("${JAVA_HOME}"/bin/jdeps -q \
  --multi-release "${JAVA_VERSION}" \
  --ignore-missing-deps \
  --print-module-deps \
  --class-path "${TARGET_DIRECTORY}/libs/*" \
  "${MAIN_CLASSPATH}")
  echo "$result,jdk.unsupported,jdk.crypto.ec,jdk.localedata,java.naming,java.scripting,java.xml"
}

function create_runtime() {
  "${JAVA_HOME}"/bin/jlink \
  --strip-native-commands \
  --no-header-files \
  --no-man-pages  \
  --compress=2  \
  --strip-debug \
  --add-modules "$2" \
  --include-locales=en,fr \
  --output "$1"
}

function create_package() {
  if [ "${OS}" = 'ubuntu-latest' ]; then
    "${JAVA_HOME}"/bin/jpackage \
      --type app-image \
      --verbose \
      --dest "$2" \
      --input "${TARGET_DIRECTORY}/libs" \
      --name MowItNow \
      --main-class "${MAIN_CLASS}" \
      --main-jar "${MAIN_JAR}" \
      --java-options -Xmx2048m \
      --java-options -Dprism.maxvram=2G \
      --arguments "--mower-latency-ms=500" \
      --runtime-image "$1" \
      --icon "${ICON}" \
      --app-version "${PROJECT_VERSION%-SNAPSHOT}" \
      --vendor "Edwyn Tech" \
      --copyright "Copyright © 2024 Edwyn Tech"
  elif [ "${OS}" = 'macos-latest' ]; then
    "${JAVA_HOME}"/bin/jpackage \
      --type dmg \
      --verbose \
      --dest "$2" \
      --input "${TARGET_DIRECTORY}/libs" \
      --name MowItNow \
      --main-class "${MAIN_CLASS}" \
      --main-jar "${MAIN_JAR}" \
      --java-options -Xmx2048m \
      --java-options -Dprism.maxvram=2G \
      --arguments "--mower-latency-ms=500" \
      --runtime-image "$1" \
      --icon "${ICON}" \
      --app-version "${PROJECT_VERSION%-SNAPSHOT}" \
      --vendor "Edwyn Tech" \
      --copyright "Copyright © 2024 Edwyn Tech"
  elif [ "${OS}" = 'windows-latest' ]; then
    "${JAVA_HOME}"/bin/jpackage \
      --type msi \
      --verbose \
      --dest "$2" \
      --input "${TARGET_DIRECTORY}/libs" \
      --name MowItNow \
      --main-class "${MAIN_CLASS}" \
      --main-jar "${MAIN_JAR}" \
      --java-options -Xmx2048m \
      --java-options -Dprism.maxvram=2G \
      --arguments "--mower-latency-ms=500" \
      --runtime-image "$1" \
      --icon "${ICON}" \
      --app-version "${PROJECT_VERSION%-SNAPSHOT}" \
      --vendor "Edwyn Tech" \
      --copyright "Copyright © 2024 Edwyn Tech" \
      --about-url "https://github.com/Edwyntech/mowitnow" \
      --win-dir-chooser \
      --win-shortcut \
      --win-per-user-install \
      --win-menu
  fi
}

rm -rfd "${ROOT_DIR}/target/java-runtime"
rm -rfd "${ROOT_DIR}/target/installer"
cp "${TARGET_DIRECTORY}/${MAIN_JAR}" "${TARGET_DIRECTORY}/libs"

MODULES=$(list_modules)
echo "Modules: ${MODULES}"
create_runtime "${ROOT_DIR}/target/java-runtime" "${MODULES}"
create_package "${ROOT_DIR}/target/java-runtime" "${ROOT_DIR}/target/installer"
echo "An installer for ${OS} is available at ${ROOT_DIR}/target/installer"
