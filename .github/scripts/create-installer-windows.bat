@ECHO OFF

:: expected environment variables:
:: JAVA_VERSION - Java version in maven format (17,21,...)
:: JAVA_HOME - JDK home
echo JAVA_VERSION: %JAVA_VERSION%
echo JAVA_HOME: %JAVA_HOME%

FOR /F "tokens=*" %%g IN ('mvn -f ui-desktop\pom.xml validate help:evaluate "-Dexpression=project.rootDirectory" -q -DforceStdout') do (SET ROOT_DIR=%%g)
FOR /F "tokens=*" %%g IN ('mvn -f ui-desktop\pom.xml validate help:evaluate "-Dexpression=project.build.directory" -q -DforceStdout') do (SET TARGET_DIRECTORY=%%g)
FOR /F "tokens=*" %%g IN ('mvn -f ui-desktop\pom.xml validate help:evaluate "-Dexpression=project.build.outputDirectory" -q -DforceStdout') do (SET CLASSES_DIRECTORY=%%g)
FOR /F "tokens=*" %%g IN ('mvn -f ui-desktop\pom.xml validate help:evaluate "-Dexpression=project.version" -q -DforceStdout') do (SET PROJECT_VERSION=%%g)
FOR /F "tokens=*" %%g IN ('mvn -f ui-desktop\pom.xml validate help:evaluate "-Dexpression=project.build.finalName" -q -DforceStdout') do (SET MAIN_JAR=%%g.jar)
FOR /F "tokens=*" %%g IN ('mvn -f ui-desktop\pom.xml validate help:evaluate "-Dexpression=main.class" -q -DforceStdout') do (SET MAIN_CLASS=%%g)
SET APPLICATION_ICON="%ROOT_DIR%\.github\assets\icons\windows\mowitnow.ico"

echo ROOT_DIR: %ROOT_DIR%
echo TARGET_DIRECTORY: %TARGET_DIRECTORY%
echo CLASSES_DIRECTORY: %CLASSES_DIRECTORY%
echo PROJECT_VERSION: %PROJECT_VERSION%
echo MAIN_JAR: %MAIN_JAR%
echo MAIN_CLASS: %MAIN_CLASS%
echo MAIN_CLASSPATH: %MAIN_CLASSPATH%
echo APPLICATION_ICON: %APPLICATION_ICON%

IF EXIST %ROOT_DIR%\target\java-runtime rmdir /S /Q  %ROOT_DIR%\target\java-runtime
IF EXIST %ROOT_DIR%\target\installer rmdir /S /Q %ROOT_DIR%\target\installer

xcopy /S /Q target\libs\* target\installer\input\libs\
copy %TARGET_DIRECTORY%\%MAIN_JAR% %TARGET_DIRECTORY%\libs\

"%JAVA_HOME%\bin\jdeps" ^
  -q ^
  --multi-release %JAVA_VERSION% ^
  --ignore-missing-deps ^
  --class-path "%TARGET_DIRECTORY%\libs\*" ^
  --print-module-deps %MAIN_CLASS_PATH% > dependencies.txt

set /p detected_modules=<dependencies.txt
set manual_modules=,jdk.crypto.ec,jdk.localedata

call "%JAVA_HOME%\bin\jlink" ^
  --strip-native-commands ^
  --no-header-files ^
  --no-man-pages ^
  --compress=2 ^
  --strip-debug ^
  --add-modules %detected_modules%%manual_modules% ^
  --include-locales=en,fr ^
  --output target/java-runtime

call "%JAVA_HOME%\bin\jpackage" ^
  --type msi ^
  --dest target/installer ^
  --input target/installer/input/libs ^
  --name MowItNow ^
  --main-class %MAIN_CLASS% ^
  --main-jar %MAIN_JAR% ^
  --arguments -Dprism.maxvram=2G ^
  --java-options -Xmx2048m ^
  --runtime-image target/java-runtime ^
  --icon %APPLICATION_ICON% ^
  --app-version %PROJECT_VERSION:-SNAPSHOT=% ^
  --vendor "Edwyn Tech" ^
  --copyright "Copyright © 2024 Edwyn Tech" ^
  --win-dir-chooser ^
  --win-shortcut ^
  --win-per-user-install ^
  --win-menu
