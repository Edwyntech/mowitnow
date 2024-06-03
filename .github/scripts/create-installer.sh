#!/bin/bash

# expected environment variables:
# RUNNER - The runner this script is running on (ubuntu-latest, windows-latest, macos-latest)
# JAVA_VERSION - Java version in maven format (17,21,...)
# JAVA_HOME - JDK home
echo "JAVA_VERSION: ${JAVA_VERSION}"
echo "JAVA_HOME: ${JAVA_HOME}"

function mvn_evaluate() {
    result=$(mvn -f ui-desktop/pom.xml validate help:evaluate -Dexpression="$1" -q -DforceStdout)
    echo "$result"
}

ROOT_DIR=$(mvn_evaluate project.rootDirectory)
TARGET_DIRECTORY=$(mvn_evaluate project.build.directory)
PROJECT_VERSION=$(mvn_evaluate project.version)
MAIN_JAR="$(mvn_evaluate project.build.finalName).jar"
MAIN_CLASS=$(mvn_evaluate main.class)
MAIN_CLASSPATH=$(mvn_evaluate main.classpath)

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
  # options are platform dependent
  declare -a platform_parameter
  if [ "${RUNNER}" = 'ubuntu-latest' ]; then
    platform_parameter+=("--icon ${ROOT_DIR}/.github/assets/icons/mowitnow.png")
  elif [ "${RUNNER}" = 'macos-latest' ]; then
    platform_parameter+=("--icon ${ROOT_DIR}/.github/assets/icons/mowitnow.icns")
    platform_parameter+=("--mac-package-identifier tech.edwyn.mowitnow")
    platform_parameter+=("--mac-package-name EdwynTech")
  elif [ "${RUNNER}" = 'macos-latest' ]; then
    platform_parameter+=("--icon ${ROOT_DIR}/.github/assets/icons/mowitnow.ico")
    platform_parameter+=("--win-dir-chooser")
    platform_parameter+=("--win-shortcut")
    platform_parameter+=("--win-per-user-install")
    platform_parameter+=("--win-menu")
  fi

  "${JAVA_HOME}"/bin/jpackage \
  --dest "$2" \
  --input "${TARGET_DIRECTORY}/libs" \
  --name MowItNow \
  --main-class "${MAIN_CLASS}" \
  --main-jar "${MAIN_JAR}" \
  --arguments -Dprism.maxvram=2G \
  --java-options -Xmx2048m \
  --runtime-image "$1" \
  --app-version "${PROJECT_VERSION%-SNAPSHOT}" \
  --vendor "Edwyn Tech" \
  --copyright "Copyright © 2024 Edwyn Tech" \
  "${platform_parameter[@]}"
}

rm -rfd "${ROOT_DIR}/target/java-runtime"
rm -rfd "${ROOT_DIR}/target/installer"
cp "${TARGET_DIRECTORY}/${MAIN_JAR}" "${TARGET_DIRECTORY}/libs"

create_runtime "${ROOT_DIR}/target/java-runtime"
create_installer "${ROOT_DIR}/target/java-runtime" "${ROOT_DIR}/target/installer"
echo "An installer for ${RUNNER} is available at ${ROOT_DIR}/target/installer"
