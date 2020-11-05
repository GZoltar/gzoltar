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
import { Folder } from './folder';
import { Maven, BuildTool, Gradle } from './buildTool';

export class FolderContainer {

    private static readonly CONFIG_FOLDER: string = '.gzoltar/';

    private readonly toolsPath: string;
    private readonly folders: { [key: string]: Folder };

    public constructor(toolsPath: string, folders: string[]) {
        this.toolsPath = toolsPath;
        this.folders = {};
        this.addFolders(folders);
    }

    public getFolder(key: string): Folder {
        return this.folders[key];
    }

    public getFolders(): Folder[] {
        return Object.values(this.folders);
    }
    
    public async updateFolders(addedFolders: string[], removedFolders: string[]): Promise<void> {
        this.removeFolders(removedFolders);
        await this.addFolders(addedFolders);
    }

    private async addFolders(addedFolders: string[]): Promise<void> {
        await Promise.all(addedFolders.map(w => this.createFolder(w)));
    }

    private removeFolders(removedWorkspaces: string[]) {
        removedWorkspaces.forEach(w => {
            this.folders[w].dispose();
            delete this.folders[w];
        });
    }

    private async createFolder(path: string): Promise<void> {
        const configFolderPath = join(path, FolderContainer.CONFIG_FOLDER);
        const tool = this.getBuildTool(path);

        if (!tool) {
            return;
        }

        if (!(await fse.pathExists(configFolderPath))) {
            await fse.copy(this.toolsPath, configFolderPath, { overwrite: false });
        }

        this.folders[path] = new Folder(path, tool, configFolderPath);
    }

    private getBuildTool(folder: string): BuildTool | undefined {
        if (fse.pathExistsSync(join(folder, 'pom.xml'))) {
            return new Maven();
        }
        if (fse.pathExistsSync(join(folder, 'build.gradle'))) {
            return new Gradle();
        }
    }
}