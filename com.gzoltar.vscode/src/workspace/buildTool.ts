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

import * as fse from 'fs-extra';
import { join } from 'path';
import { Command, CommandRet } from '../cmdLine/command';

export { BuildTool, Maven, Gradle };

interface BuildTool {
    
    getSourceFolder(): string;
    getTestFolder(): string;
    getDependencies(projectPath: string): Promise<CommandRet | string>;
    runTests(projectPath: string): Promise<CommandRet>;
    
}

class Maven implements BuildTool {

    getSourceFolder(): string {
        return '/target/classes';
    }

    getTestFolder(): string {
        return '/target/test-classes';
    }

    async getDependencies(projectPath: string): Promise<CommandRet | string> {
        const ret = await Command.exec(`(cd ${projectPath} && mvn dependency:build-classpath -Dmdep.outputFile="cp.txt")`);
        if (ret.failed) {
            return ret;
        }
        const dep = (await fse.readFile(join(projectPath, 'cp.txt'))).toString();
        return dep.replace('\n', ':');
    }

    async runTests(projectPath: string): Promise<CommandRet> {
        const ret = await Command.exec(`(cd ${projectPath} && mvn test -Dmaven.test.failure.ignore=true -Dmaven.test.error.ignore=true)`);
        return ret;
    }
}

class Gradle implements BuildTool {

    private readonly REGEX = /(?:\\\-\-\-|\+\-\-\-)(.+)/;

    getSourceFolder(): string {
        return '/build/classes/java/main';
    }

    getTestFolder(): string {
        return '/build/classes/java/test';
    }

    async getDependencies(projectPath: string): Promise<CommandRet | string> {
        const ret = await Command.exec(`(cd ${projectPath} && gradle -q dependencies --configuration testRuntimeClasspath)`);
        if (ret.failed) {
            return ret;
        }
        const split = ret.stdout.split(this.REGEX);
        return split
            .filter((_, idx) => idx % 2 !== 0)
            .map(s => `"${s.trim()}"`)
            .join(':');
    }

    async runTests(projectPath: string): Promise<CommandRet> {
        const ret = await Command.exec(`(cd ${projectPath} && gradle test)`);
        return ret;
    }
}