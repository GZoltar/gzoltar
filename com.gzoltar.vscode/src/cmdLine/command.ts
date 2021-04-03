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

const exec = require('util').promisify(require('child_process').exec);

export class Command {
    
    private readonly commands: string[];
    private readonly cpSep: string;

    constructor() {
        this.commands = [];
        this.cpSep = process.platform === 'win32' ? ';' : ':';
    }

    public newCmd(): Command {
        this.commands.push('&&');
        return this;
    }

    public cd(dest: string): Command {
        this.commands.push(`cd ${dest}`);
        return this;
    }

    public java(): Command {
        this.commands.push('java');
        return this;
    }

    public javap(classFile: string): Command {
        this.commands.push(`javap -verbose ${classFile}`);
        return this;
    }

    public javaagent(agentJar: string): Command {
        this.commands.push(`-javaagent:${agentJar}`);
        return this;
    }

    public cp(...args: string[]): Command {
        this.commands.push(`-cp ${args.join(this.cpSep)}`);
        return this;
    }
    public main(mainArgs: string): Command {
        this.commands.push(mainArgs);
        return this;
    }

    public toString(): string {
        return `(${this.commands.join(' ')})`;
    }

    public static async exec(cmd: string) {
        let failed = false;
        let stdout_str = "";
        let stderr_str = "";

        try {
            const { stdout, stderr } = await exec(cmd);
            stdout_str = `${stdout}`;
            stderr_str = `${stderr}`;
        } catch (error) {
            failed = true;
            stderr_str = `${error}`;
        }

        return new CommandRet(failed, stdout_str, stderr_str);
    }
}

export class CommandRet {

    public readonly failed: boolean;
    public readonly stdout: string;
    public readonly stderr: string;

    constructor(failed: boolean, stdout: string, stderr: string) {
        this.failed = failed;
        this.stdout = stdout;
        this.stderr = stderr;
    }

}
