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
    return new Command()
        .cd(destPath)
        .newCmd()
        .java()
        .cp('"."', '"gzoltarcli.jar"')
        .main(`com.gzoltar.cli.Main faultLocalizationReport --buildLocation "build/" --dataFile gzoltar.ser --granularity "line" --family "sfl" --formula "ochiai" --outputDirectory . --formatter HTML,TXT`)
        .toString();
}

function disassemble(destPath: string, classFile: string): string {
    return new Command()
        .cd(destPath)
        .newCmd()
        .javap(classFile)
        .toString();
}