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
import { ReportPanel } from '../reportPanel';
import { Decorator } from '../decoration/decorator';
import { BuildTool } from './buildTool';
import { CommandRet } from '../cmdLine/command';
import { Webview } from 'vscode';

export class Folder {

    private readonly path: string;
    private readonly buildTool: BuildTool;
    private readonly configFolder: string;

    private webview?: ReportPanel;
    private decorator?: Decorator;

    public constructor(path: string, buildTool: BuildTool, configFolder: string) {
        this.path = path;
        this.buildTool = buildTool;
        this.configFolder = configFolder;
    }

    get folderPath(): string {
        return this.path;
    }

    get configPath(): string {
        return this.configFolder;
    }

    get sourceFolder(): string {
        return join(this.path, this.buildTool.getSourceFolder());
    }

    get testFolder(): string {
        return join(this.path, this.buildTool.getTestFolder());
    }

    public getWebview(): ReportPanel | undefined {
        return this.webview;
    }

    public setWebview(newWebview: ReportPanel): void {
        this.webview?.dispose();
        this.webview = newWebview;
    }

    public resetWebview(): void {
        this.webview = undefined;
    }

    public setDecorator(newDecorator: Decorator): void {
        this.decorator?.dispose();
        this.decorator = newDecorator;
    }

    public dispose(): void {
        this.webview?.dispose();
        this.webview = undefined;
        this.decorator?.dispose();
        this.decorator = undefined;
    }

    public async resetConfig(toolsPath: string): Promise<void> {
        await fse.emptyDir(this.configFolder);
        await fse.copy(toolsPath, this.configFolder, { overwrite: false });
    }

    public async cleanup(): Promise<void> {
        await Promise.all([
            fse.emptyDir(join(this.configFolder, 'build')),
            fse.emptyDir(join(this.configFolder, 'sfl')),
            fse.remove(join(this.configFolder, 'tests.txt')),
            fse.remove(join(this.configFolder, 'gzoltar.ser'))
        ]);
    }

    public async copyToBuild(): Promise<void> {
        const buildPath = join(this.configFolder, 'build');
        const options = { overwrite: false, errorOnExist: false };
        await fse.copy(this.sourceFolder, buildPath, options);
        await fse.copy(this.testFolder, buildPath, options);

        const mainRes = join('src', 'main', 'resources');
        const testRes = join('src', 'test', 'resources');
        if ((await fse.pathExists(join(this.path, mainRes)))) {
            await fse.copy(join(this.path, mainRes), join(this.configFolder, mainRes), options);
        }
        if ((await fse.pathExists(join(this.path, testRes)))) {
            await fse.copy(join(this.path, testRes), join(this.configFolder, testRes), options);
        }
    }

    public async getDependencies(): Promise<CommandRet | string> {
        return this.buildTool.getDependencies(this.path);
    }

    public async runTests(): Promise<CommandRet> {
        return this.buildTool.runTests(this.path);
    }

    public async getIncludes(): Promise<string> {
        return (await this.getFiles(this.sourceFolder, ''))
            .map(f => f.replace(/.class/g, ''))
            .join(':');
    }

    private async getFiles(dir: string, prefix: string): Promise<string[]> {
        const result = await fse.readdir(dir);
        let filelist = Array<string>();

        for (const file of result) {
            if ((await fse.stat(dir + '/' + file)).isDirectory()) {
                const subFiles = await this.getFiles(`${dir}/${file}`, `${prefix + file}.`);
                filelist = filelist.concat(subFiles);
            }
            else {
                filelist.push(prefix + file);
            }
        }

        return filelist;
    }
}