#!/bin/bash

# expected environment variables:
# RUNNER - The runner this script is running on (ubuntu-latest, windows-latest, macos-latest)
# JAVA_VERSION - Java version in maven format (17,21,...)
# JAVA_HOME - JDK home
echo "RUNNER: ${RUNNER}"
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

echo "ROOT_DIR: ${ROOT_DIR}"
echo "TARGET_DIRECTORY: ${TARGET_DIRECTORY}"
echo "PROJECT_VERSION: ${PROJECT_VERSION}"
echo "MAIN_JAR: ${MAIN_JAR}"
echo "MAIN_CLASS: ${MAIN_CLASS}"
echo "MAIN_CLASSPATH: ${MAIN_CLASSPATH}"

function list_modules() {
  result=$("${JAVA_HOME}"/bin/jdeps -q \
  --multi-release "${JAVA_VERSION}" \
  --ignore-missing-deps \
  --print-module-deps \
  --class-path "${TARGET_DIRECTORY}/libs/*" \
  "${MAIN_CLASSPATH}")
  echo "$result,jdk.crypto.ec,jdk.localedata"
}

function create_runtime() {
  "${JAVA_HOME}"/bin/jlink \
  --strip-native-commands \
  --no-header-files \
  --no-man-pages  \
  --compress=2  \
  --strip-debug \
  --add-modules "$(list_modules)" \
  --include-locales=en,fr \
  --output "$1"
}

function create_installer() {
  if [ "${RUNNER}" = 'ubuntu-latest' ]; then
    "${JAVA_HOME}"/bin/jpackage \
      --type app-image \
      --dest "$2" \
      --input "${TARGET_DIRECTORY}/libs" \
      --app-content "${TARGET_DIRECTORY}/cukedoctor/index.pdf"\
      --name MowItNow \
      --main-class "${MAIN_CLASS}" \
      --main-jar "${MAIN_JAR}" \
      --arguments -Dprism.maxvram=2G \
      --java-options -Xmx2048m \
      --runtime-image "$1" \
      --icon "${ROOT_DIR}/.github/assets/icons/mowitnow.png" \
      --app-version "${PROJECT_VERSION%-SNAPSHOT}" \
      --vendor "Edwyn Tech" \
      --copyright "Copyright © 2024 Edwyn Tech"
  elif [ "${RUNNER}" = 'macos-latest' ]; then
    "${JAVA_HOME}"/bin/jpackage \
      --type dmg \
      --dest "$2" \
      --input "${TARGET_DIRECTORY}/libs" \
      --app-content "${TARGET_DIRECTORY}/cukedoctor/index.pdf"\
      --name MowItNow \
      --main-class "${MAIN_CLASS}" \
      --main-jar "${MAIN_JAR}" \
      --arguments -Dprism.maxvram=2G \
      --java-options -Xmx2048m \
      --runtime-image "$1" \
      --icon "${ROOT_DIR}/.github/assets/icons/mowitnow.icns" \
      --app-version "${PROJECT_VERSION%-SNAPSHOT}" \
      --vendor "Edwyn Tech" \
      --copyright "Copyright © 2024 Edwyn Tech" \
      --about-url "https://github.com/Edwyntech/mowitnow" \
      --mac-package-identifier tech.edwyn.mowitnow \
      --mac-package-name EdwynTech
  elif [ "${RUNNER}" = 'windows-latest' ]; then
    "${JAVA_HOME}"/bin/jpackage \
      --type msi \
      --dest "$2" \
      --input "${TARGET_DIRECTORY}/libs" \
      --app-content "${TARGET_DIRECTORY}\cukedoctor\index.pdf"\
      --name MowItNow \
      --main-class "${MAIN_CLASS}" \
      --main-jar "${MAIN_JAR}" \
      --arguments -Dprism.maxvram=2G \
      --java-options -Xmx2048m \
      --runtime-image "$1" \
      --icon "${ROOT_DIR}\.github\assets\icons\mowitnow.ico" \
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

create_runtime "${ROOT_DIR}/target/java-runtime"
create_installer "${ROOT_DIR}/target/java-runtime" "${ROOT_DIR}/target/installer"
echo "An installer for ${RUNNER} is available at ${ROOT_DIR}/target/installer"
