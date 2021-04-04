/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
'use strict';

import { Command } from "./command";

export { listFunction, runFunction, reportFunction, disassemble };

function listFunction(destPath: string, dependencies: string, testFolder: string): string {
    return new Command()
        .cd(destPath)
        .newCmd()
        .java()
        .cp('"build/"', dependencies, '"hamcrest-core.jar"', '"gzoltarcli.jar"')
        .main(`com.gzoltar.cli.Main listTestMethods ${testFolder}`)
        .toString();
}

function runFunction(destPath: string, dependencies: string, includes: string): string {
    return new Command()
        .cd(destPath)
        .newCmd()
        .java()
        .javaagent(`gzoltaragent.jar=includes="${includes}"`)
        .cp('"build/"', dependencies, '"junit.jar"', '"hamcrest-core.jar"', '"gzoltarcli.jar"')
        .main(`com.gzoltar.cli.Main runTestMethods --testMethods "tests.txt" --collectCoverage`)
        .toString();
}

function reportFunction(destPath: string, publMethods: boolean, staticConstr: boolean, deprMethods: boolean): string {
    let additionalArgs = '';
    if (publMethods) {
        additionalArgs = additionalArgs + ' --inclPublicMethods';
    }
    if (staticConstr) {
        additionalArgs = additionalArgs + ' --inclStaticConstructors';
    }
    if (deprMethods) {
        additionalArgs = additionalArgs + ' --inclDeprecatedMethods';
    }
    return new Command()
        .cd(destPath)
        .newCmd()
        .java()
        .cp('"."', '"gzoltarcli.jar"')
        .main(`com.gzoltar.cli.Main faultLocalizationReport --buildLocation "build/" --dataFile gzoltar.ser --granularity "line" --family "sfl" --formula "ochiai" --outputDirectory . --formatter "txt:html"` + additionalArgs)
        .toString();
}

function disassemble(destPath: string, classFile: string): string {
    return new Command()
        .cd(destPath)
        .newCmd()
        .javap(classFile)
        .toString();
}