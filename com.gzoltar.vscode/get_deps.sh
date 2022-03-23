#!/usr/bin/env bash
#
#  Copyright (C) 2020 GZoltar contributors.
#
#  This file is part of GZoltar.
#
#  GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
#  Lesser General Public License as published by the Free Software Foundation, either version 3 of
#  the License, or (at your option) any later version.
#
#  GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
#  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
#  General Public License for more details.
#
#  You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
#  not, see <https://www.gnu.org/licenses/>.
#
# ------------------------------------------------------------------------------
# This script collects all required dependencies, i.e.,:
#   - GZoltar's Agent
#   - GZoltar's CLI
#   - JUnit v4.13
#   - Hamcrest Core v2.2
#
# Usage:
# ./get_deps.sh
#
# Requirements:
# - [Apache Maven](https://maven.apache.org)
# - [GNU Wget](https://www.gnu.org/software/wget)
#
# ------------------------------------------------------------------------------

SCRIPT_DIR=$(cd `dirname $0` && pwd)

#
# Print error message and exit
#
die() {
  echo "$@" >&2
  exit 1
}

# ------------------------------------------------------------------ Envs & Args

# Check whether Apache Maven is installed and available
mvn --version > /dev/null 2>&1 || die "mvn (https://maven.apache.org) is not available!"
# Check whether GNU Wget is installed and available
wget --version > /dev/null 2>&1 || die "mvn (https://maven.apache.org) is not available!"

DEPS_DIR="$SCRIPT_DIR/tools"
mkdir -p "$DEPS_DIR"

# ------------------------------------------------------------------------- Main

##
# 1. Compile GZoltar

pushd . > /dev/null 2>&1
cd "$SCRIPT_DIR/../"
  mvn clean package -DskipTests=true || die "Failed to compile GZoltar!"
popd > /dev/null 2>&1

##
# 2. Copy GZoltar's artefacts

GZOLTAR_CLI_JAR="$SCRIPT_DIR/../com.gzoltar.cli/target/com.gzoltar.cli-1.7.4-SNAPSHOT-jar-with-dependencies.jar"
[ -s "$GZOLTAR_CLI_JAR" ] || die "$GZOLTAR_CLI_JAR does not exist or it is empty!"

GZOLTAR_AGENT_JAR="$SCRIPT_DIR/../com.gzoltar.agent/target/classes/gzoltaragent.jar"
[ -s "$GZOLTAR_AGENT_JAR" ] || die "$GZOLTAR_AGENT_JAR does not exist or it is empty!"

cp -v "$GZOLTAR_CLI_JAR" "$DEPS_DIR/gzoltarcli.jar" || die "Failed to copy $GZOLTAR_CLI_JAR to $DEPS_DIR/!"
cp -v "$GZOLTAR_AGENT_JAR" "$DEPS_DIR/gzoltaragent.jar" || die "Failed to copy $GZOLTAR_AGENT_JAR to $DEPS_DIR/!"

##
# 3. Get external dependencies

wget -O "$DEPS_DIR/junit.jar" https://repo1.maven.org/maven2/junit/junit/4.13/junit-4.13.jar || die "Failed to get JUnit from the maven repository!"
[ -s "$DEPS_DIR/junit.jar" ] || die "$DEPS_DIR/junit.jar does not exist or it is empty!"

wget -O "$DEPS_DIR/hamcrest-core.jar" https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/2.2/hamcrest-core-2.2.jar || die "Failed to get Hamcrest Core from the maven repository!"
[ -s "$DEPS_DIR/hamcrest-core.jar" ] || die "$DEPS_DIR/hamcrest-core.jar does not exist or it is empty!"

echo "DONE!" >&2
exit 0
