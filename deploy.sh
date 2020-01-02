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
# 
# This script deploys GZoltar to the Nexus Repository Manager.
#
# Usage: ./deploy.sh
#
# Requirements:
#  - GZoltar GPG Key
#  - The following configuration in the ~/.m2/settings.xml file:
#      <settings>
#        <servers>
#          <server>
#            <id>sonatype-nexus-staging</id>
#            <username>__GZoltar_USERNAME__</username>
#            <password>__GZoltar_GPG_KEY_PASSWORD__</password>
#          </server>
#          <server>
#            <id>sonatype-nexus-snapshots</id>
#            <username>__GZoltar_USERNAME__</username>
#            <password>__GZoltar_GPG_KEY_PASSWORD__</password>
#          </server>
#        </servers>
#        <profiles>
#          <profile>
#            <activation>
#              <activeByDefault>true</activeByDefault>
#            </activation>
#            <properties>
#              <gpg.executable>gpg</gpg.executable>
#              <gpg.keyname>__GZoltar_GPG_KEY_ID__</gpg.keyname>
#            </properties>
#          </profile>
#        </profiles>
#      </settings>
#
# ------------------------------------------------------------------------------

SCRIPT_DIR=$(cd `dirname $0` && pwd)

#
# Prints error message to the stdout and exit.
#
die() {
  echo "$@" >&2
  exit 1
}

if ! mvn --version > /dev/null 2>&1; then
  die "Apache maven not found. Please install it from https://maven.apache.org/ and re-run the script."
fi

pushd . > /dev/null 2>&1
cd "$SCRIPT_DIR/com.gzoltar.build"
  mvn clean -P release deploy || die "Deploy has failed!"
popd > /dev/null 2>&1

echo "DONE!"
exit 0
