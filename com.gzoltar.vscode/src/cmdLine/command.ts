'use strict';

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
}
