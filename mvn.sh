#!/bin/sh
# Maven wrapper for Windows + Git Bash (MSYS) + native Windows JVM.
# 用法: ./mvn.sh <maven args>  (例如 ./mvn.sh clean install -DskipTests)
# 可用环境变量覆盖: JAVA=D:/path/to/jdk/bin/java  M2=D:/path/to/apache-maven

set -e

: "${JAVA:=D:/ProgramFiles/Java/jdk-17/bin/java}"
: "${M2:=D:/MAVEN/apache-maven-3.9.9}"

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd -W 2>/dev/null || pwd)"

JAR="$M2/boot/plexus-classworlds-2.8.0.jar"
if [ ! -f "$JAR" ]; then
  echo "ERROR: classworlds jar not found at $JAR" >&2
  exit 1
fi
if [ ! -x "$JAVA" ]; then
  echo "ERROR: java not found at $JAVA" >&2
  exit 1
fi

exec "$JAVA" \
  -Dmaven.resolver.transport=wagon \
  -Dmaven.wagon.http.ssl.insecure=true \
  -Dmaven.wagon.http.ssl.allowall=true \
  -Dmaven.wagon.http.ssl.ignore.validity.dates=true \
  -classpath "$JAR" \
  "-Dclassworlds.conf=$M2/bin/m2.conf" \
  "-Dmaven.home=$M2" \
  "-Dmaven.multiModuleProjectDirectory=$PROJECT_DIR" \
  org.codehaus.plexus.classworlds.launcher.Launcher "$@"
