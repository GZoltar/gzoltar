#!/usr/bin/env bash
#
# ------------------------------------------------------------------------------
# This script performs fault-localization on a Java project using the GZoltar
# command line interface either using instrumentation 'at runtime' or 'offline'.
#
# Usage:
# ./run.sh
#     --instrumentation <online|offline>
#     [--help]
#
# Requirements:
# - `java` and `javac` needs to be set and must point to the Java installation.
#
# ------------------------------------------------------------------------------

SCRIPT_DIR=$(cd `dirname ${BASH_SOURCE[0]}` && pwd)

#
# Print error message and exit
#
die() {
  echo "$@" >&2
  exit 1
}

# ------------------------------------------------------------------ Envs & Args

GZOLTAR_VERSION="1.7.4-SNAPSHOT"

# Check whether GZOLTAR_CLI_JAR is set
export GZOLTAR_CLI_JAR="$SCRIPT_DIR/../com.gzoltar.cli/target/com.gzoltar.cli-$GZOLTAR_VERSION-jar-with-dependencies.jar"
[ "$GZOLTAR_CLI_JAR" != "" ] || die "GZOLTAR_CLI is not set!"
[ -s "$GZOLTAR_CLI_JAR" ] || die "$GZOLTAR_CLI_JAR does not exist or it is empty! Please go to '$SCRIPT_DIR/..' and run 'mvn clean install'."

# Check whether GZOLTAR_AGENT_RT_JAR is set
export GZOLTAR_AGENT_RT_JAR="$SCRIPT_DIR/../com.gzoltar.agent.rt/target/com.gzoltar.agent.rt-$GZOLTAR_VERSION-all.jar"
[ "$GZOLTAR_AGENT_RT_JAR" != "" ] || die "GZOLTAR_AGENT_RT_JAR is not set!"
[ -s "$GZOLTAR_AGENT_RT_JAR" ] || die "$GZOLTAR_AGENT_RT_JAR does not exist or it is empty! Please go to '$SCRIPT_DIR/..' and run 'mvn clean install'."

USAGE="Usage: ${BASH_SOURCE[0]} --instrumentation <online|offline> [--help]"
if [ "$#" -eq "0" ]; then
  die "$USAGE"
fi
mod_of_two=$(expr $# % 2)
if [ "$#" -ne "1" ] && [ "$mod_of_two" -ne "0" ]; then
  die "$USAGE"
fi

INSTRUMENTATION=""
TARGET=""

while [[ "$1" = --* ]]; do
  OPTION=$1; shift
  case $OPTION in
    (--instrumentation)
      INSTRUMENTATION=$1;
      shift;;
    (--target)
      if [ -n "$1" ] && [ "$1" -eq "$1" ] 2>/dev/null; then
        if (( $1 >= 8 && $1 <= 19 )); then
          TARGET=$1;
          shift;
        else
          die "$USAGE";
        fi
      else
        die "$USAGE";
      fi;;
    (--help)
      echo "$USAGE";
      exit 0;;
    (*)
      die "$USAGE";;
  esac
done

[ "$INSTRUMENTATION" != "" ] || die "$USAGE"
if [ "$INSTRUMENTATION" != "online" ] && [ "$INSTRUMENTATION" != "offline" ]; then
  die "$USAGE"
fi
TARGET_STR=""
if [ "$TARGET" != "" ]; then
  # Convert target to string
  TARGET_STR="--target $TARGET"
  echo "Target is set to: $TARGET_STR"
fi
SOURCE_STR=""
if [ "$TARGET" != "" ]; then
  # Convert source to string
  SOURCE_STR="--source $TARGET"
  echo "Source is set to: $SOURCE_STR"
fi

#
# Prepare runtime dependencies
#



LIB_DIR="$SCRIPT_DIR/lib"
mkdir -p "$LIB_DIR" || die "Failed to create $LIB_DIR!"
[ -d "$LIB_DIR" ] || die "$LIB_DIR does not exist!"

JUNIT4_JAR="$LIB_DIR/junit4.jar"
if [ ! -s "$JUNIT4_JAR" ]; then
  wget "https://repo1.maven.org/maven2/junit/junit/4.12/junit-4.12.jar" -O "$JUNIT4_JAR" || die "Failed to get junit-4.12.jar from https://repo1.maven.org!"
fi
[ -s "$JUNIT4_JAR" ] || die "$JUNIT4_JAR does not exist or it is empty!"

JUNIT_JAR="$LIB_DIR/junit.jar"
if [ ! -s "$JUNIT_JAR" ]; then
  wget "https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-api/5.9.2/junit-jupiter-api-5.9.2.jar" -O "$JUNIT_JAR" || die "Failed to get junit-4.12.jar from https://repo1.maven.org!"
fi
[ -s "$JUNIT_JAR" ] || die "$JUNIT_JAR does not exist or it is empty!"

JUNIT_PARAM_JAR="$LIB_DIR/junit-params.jar"
if [ ! -s "$JUNIT_PARAM_JAR" ]; then
  wget "https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-params/5.9.2/junit-jupiter-params-5.9.2.jar" -O "$JUNIT_PARAM_JAR" || die "Failed to get junit-4.12.jar from https://repo1.maven.org!"
fi
[ -s "$JUNIT_PARAM_JAR" ] || die "$JUNIT_PARAM_JAR does not exist or it is empty!"

JUNIT_ENGINE="$LIB_DIR/junit-engine.jar"
if [ ! -s "$JUNIT_ENGINE" ]; then
  wget "https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-engine/5.9.2/junit-jupiter-engine-5.9.2.jar" -O "$JUNIT_ENGINE" || die "Failed to get junit-4.12.jar from https://repo1.maven.org!"
fi
[ -s "$JUNIT_ENGINE" ] || die "$JUNIT_ENGINE does not exist or it is empty!"

JUNIT_PLATFORM_ENGINE="$LIB_DIR/junit-platform-engine.jar"
if [ ! -s "$JUNIT_PLATFORM_ENGINE" ]; then
  wget "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-engine/1.9.2/junit-platform-engine-1.9.2.jar" -O "$JUNIT_PLATFORM_ENGINE" || die "Failed to get junit-4.12.jar from https://repo1.maven.org!"
fi
[ -s "$JUNIT_PLATFORM_ENGINE" ] || die "$JUNIT_PLATFORM_ENGINE does not exist or it is empty!"

JUNIT_PLATFORM_COMMONS="$LIB_DIR/junit-platform-commons.jar"
if [ ! -s "$JUNIT_PLATFORM_COMMONS" ]; then
  wget -np -nv "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-commons/1.9.2/junit-platform-commons-1.9.2.jar" -O "$JUNIT_PLATFORM_COMMONS" || die "Failed to get hamcrest-core-1.3.jar from https://repo1.maven.org!"
fi
[ -s "$JUNIT_PLATFORM_COMMONS" ] || die "$JUNIT_PLATFORM_COMMONS does not exist or it is empty!"

JUNIT_PLATFORM_LAUNCHER="$LIB_DIR/junit-platform-launcher.jar"
if [ ! -s "$JUNIT_PLATFORM_LAUNCHER" ]; then
  wget -np -nv "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-launcher/1.9.2/junit-platform-launcher-1.9.2.jar" -O "$JUNIT_PLATFORM_LAUNCHER" || die "Failed to get hamcrest-core-1.3.jar from https://repo1.maven.org!"
fi
[ -s "$JUNIT_PLATFORM_LAUNCHER" ] || die "$JUNIT_PLATFORM_LAUNCHER does not exist or it is empty!"

HAMCREST_JAR="$LIB_DIR/hamcrest-core.jar"
if [ ! -s "$HAMCREST_JAR" ]; then
  wget -np -nv "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" -O "$HAMCREST_JAR" || die "Failed to get hamcrest-core-1.3.jar from https://repo1.maven.org!"
fi
[ -s "$HAMCREST_JAR" ] || die "$HAMCREST_JAR does not exist or it is empty!"

BUILD_DIR="$SCRIPT_DIR/build"
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR" || die "Failed to create $BUILD_DIR!"

SRC_DIR="$SCRIPT_DIR/src"
TEST_DIR="$SCRIPT_DIR/test"

# ------------------------------------------------------------------------- Main

#
# Compile
#

echo "Compile source and test cases ..."

javac $TARGET_STR $SOURCE_STR "$SRC_DIR/org/gzoltar/examples/CharacterCounter.java" -d "$BUILD_DIR" || die "Failed to compile source code!"
javac $TARGET_STR $SOURCE_STR -cp $JUNIT4_JAR:$JUNIT_JAR:$JUNIT_PARAM_JAR:$BUILD_DIR "$TEST_DIR/org/gzoltar/examples/CharacterCounterTest.java" -d "$BUILD_DIR" || die "Failed to compile test cases!"

#
# Collect list of unit test cases to run
#

echo "Collect list of unit test cases to run ..."

UNIT_TESTS_FILE="$BUILD_DIR/tests.txt"
java -cp $BUILD_DIR:$JUNIT_ENGINE:$JUNIT4_JAR:$JUNIT_PLATFORM_ENGINE:$JUNIT_PLATFORM_COMMONS:$JUNIT_JAR:$JUNIT_PARAM_JAR:$HAMCREST_JAR:$GZOLTAR_CLI_JAR \
  com.gzoltar.cli.Main listTestMethods $BUILD_DIR \
    --outputFile "$UNIT_TESTS_FILE" \
    --includes "org.gzoltar.examples.CharacterCounterTest#*" || die "Collection of unit test cases has failed!"
[ -s "$UNIT_TESTS_FILE" ] || die "$UNIT_TESTS_FILE does not exist or it is empty!"

#
# Collect coverage
#

SER_FILE="$BUILD_DIR/gzoltar.ser"

if [ "$INSTRUMENTATION" == "online" ]; then
  echo "Perform instrumentation at runtime and run each unit test case in isolation ..."

  # Perform instrumentation at runtime and run each unit test case in isolation

  #export JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"
  java -javaagent:$GZOLTAR_AGENT_RT_JAR=destfile=$SER_FILE,buildlocation=$BUILD_DIR,includes="org.gzoltar.examples.CharacterCounter:org.gzoltar.examples.CharacterCounter\$*",excludes="",inclnolocationclasses=false,output="file" \
    -cp $JUNIT4_JAR:$BUILD_DIR:$JUNIT_ENGINE:$JUNIT_PLATFORM_ENGINE:$JUNIT_PLATFORM_COMMONS:$JUNIT_JAR:$JUNIT_PARAM_JAR:$HAMCREST_JAR:$GZOLTAR_CLI_JAR:"org.gzoltar.examples.CharacterCounterTest:org.gzoltar.examples.CharacterCounterTest\$*" \
    com.gzoltar.cli.Main runTestMethods \
      --testMethods "$UNIT_TESTS_FILE" \
      --collectCoverage || die "Coverage collection has failed!"

elif [ "$INSTRUMENTATION" == "offline" ]; then
  echo "Perform offline instrumentation ..."

  # Backup original classes
  BUILD_BACKUP_DIR="$SCRIPT_DIR/.build"
  mv "$BUILD_DIR" "$BUILD_BACKUP_DIR" || die "Backup of original classes has failed!"
  mkdir -p "$BUILD_DIR"

  # Perform offline instrumentation
  java -cp $JUNIT4_JAR:$BUILD_BACKUP_DIR:$GZOLTAR_AGENT_RT_JAR:$JUNIT_ENGINE:$JUNIT_PLATFORM_ENGINE:$JUNIT_PLATFORM_COMMONS:$JUNIT_JAR:$JUNIT_PARAM_JAR:$HAMCREST_JAR:$GZOLTAR_CLI_JAR \
    com.gzoltar.cli.Main instrument \
    --outputDirectory "$BUILD_DIR" \
    $BUILD_BACKUP_DIR || die "Offline instrumentation has failed!"

  echo "Run each unit test case in isolation ..."

  # Run each unit test case in isolation
  #export JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"
  java -cp $JUNIT4_JAR:$BUILD_DIR:$JUNIT_ENGINE:$JUNIT_PLATFORM_ENGINE:$JUNIT_PLATFORM_COMMONS:$JUNIT_JAR:$JUNIT_PARAM_JAR:$HAMCREST_JAR:$GZOLTAR_AGENT_RT_JAR:$GZOLTAR_AGENT_RT_JAR:$GZOLTAR_CLI_JAR \
    -Dgzoltar-agent.destfile=$SER_FILE \
    -Dgzoltar-agent.output="file" \
    com.gzoltar.cli.Main runTestMethods \
      --testMethods "$UNIT_TESTS_FILE" \
      --offline \
      --collectCoverage || die "Coverage collection has failed!"

  # Restore original classes
  cp -R $BUILD_BACKUP_DIR/* "$BUILD_DIR" || die "Restore of original classes has failed!"
  rm -rf "$BUILD_BACKUP_DIR"
fi

[ -s "$SER_FILE" ] || die "$SER_FILE does not exist or it is empty!"

#
# Create fault localization report
#

echo "Create fault localization report ..."

SPECTRA_FILE="$BUILD_DIR/sfl/txt/spectra.csv"
MATRIX_FILE="$BUILD_DIR/sfl/txt/matrix.txt"
TESTS_FILE="$BUILD_DIR/sfl/txt/tests.csv"

java -cp $JUNIT4_JAR:$BUILD_DIR:$JUNIT_ENGINE:$JUNIT_PLATFORM_ENGINE:$JUNIT_PLATFORM_COMMONS:$JUNIT_JAR:$JUNIT_PARAM_JAR:$HAMCREST_JAR:$GZOLTAR_CLI_JAR \
  com.gzoltar.cli.Main faultLocalizationReport \
    --buildLocation "$BUILD_DIR" \
    --granularity "line" \
    --inclPublicMethods \
    --inclStaticConstructors \
    --inclDeprecatedMethods \
    --dataFile "$SER_FILE" \
    --outputDirectory "$BUILD_DIR" \
    --family "sfl" \
    --formula "ochiai" \
    --metric "entropy" \
    --formatter "txt" || die "Generation of fault-localization report has failed!"

[ -s "$SPECTRA_FILE" ] || die "$SPECTRA_FILE does not exist or it is empty!"
[ -s "$MATRIX_FILE" ] || die "$MATRIX_FILE does not exist or it is empty!"
[ -s "$TESTS_FILE" ] || die "$TESTS_FILE does not exist or it is empty!"

echo "DONE!"
exit 0
