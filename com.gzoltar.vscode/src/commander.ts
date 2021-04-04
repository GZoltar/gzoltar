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

import * as vscode from 'vscode';
import * as fse from 'fs-extra';
import { join, basename } from 'path';
import { listFunction, runFunction, reportFunction } from './cmdLine/cmdBuilder';
import { Command, CommandRet } from './cmdLine/command';
import { ReportPanel } from './reportPanel';
import { Decorator } from './decoration/decorator';
import { FolderContainer } from './workspace/container';
import assert = require('assert');

export class GZoltarCommander implements vscode.TreeDataProvider<GZoltarCommand> {

    private _onDidChangeTreeData: vscode.EventEmitter<GZoltarCommand | undefined> = new vscode.EventEmitter<GZoltarCommand | undefined>();
    readonly onDidChangeTreeData: vscode.Event<GZoltarCommand | undefined> = this._onDidChangeTreeData.event;

    private readonly extensionPath: string;
    private readonly container: FolderContainer;
    private readonly statusBar: vscode.StatusBarItem = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right, 100);
    private readonly reportOptions: { [key: string]: boolean } = {
        'Public Methods': true,
        'Static Constructors': true,
        'Deprecated Methods': true
    };

    constructor(extensionPath: string, container: FolderContainer) {
        this.extensionPath = extensionPath;
        this.container = container;
    }

    getTreeItem(element: GZoltarCommand): vscode.TreeItem | Thenable<vscode.TreeItem> {
        return element;
    }

    getChildren(element?: GZoltarCommand | undefined): vscode.ProviderResult<GZoltarCommand[]> {
        if (!element) {
            return [
                new GZoltarCommand('OPEN FOLDERS', vscode.TreeItemCollapsibleState.Expanded),
                new GZoltarCommand('REPORT OPTIONS', vscode.TreeItemCollapsibleState.Expanded)
            ];
        }

        if (element.label === 'OPEN FOLDERS') {
            return this.container.getFolders().map(f => {
                const state = f.getWebview()? vscode.TreeItemCollapsibleState.Expanded : vscode.TreeItemCollapsibleState.None;
                return new GZoltarCommand(basename(f.folderPath), state, f.folderPath);
            });
        }

        if (element.label === 'REPORT OPTIONS') {
            const lightPath = join(this.extensionPath, 'resources', 'light');
            const darkPath = join(this.extensionPath, 'resources', 'dark');

            return Object.keys(this.reportOptions).map(key => {
                const active = this.reportOptions[key];
                const cmd = new GZoltarCommand(key, vscode.TreeItemCollapsibleState.None);
                cmd.tooltip = `Include ${key} in the Report.`;
                cmd.command = { command: 'extension.setOption', title: '', arguments: [key] };
                cmd.iconPath = {
                    light: active? join(lightPath, 'checked.svg') : join(lightPath, 'unchecked.svg'),
                    dark: active? join(darkPath, 'checked.svg') : join(darkPath, 'unchecked.svg')
                };
                return cmd;
            });
        }

        if (element.path) {
            const folder = this.container.getFolder(element.path);
            if (folder.getWebview()) {
                return [
                    new GZoltarCommand('Sunburst', vscode.TreeItemCollapsibleState.None, undefined, { command: 'extension.setView', title: '', arguments: [element.path, 0] }),
                    new GZoltarCommand('Bubble Hierarchy', vscode.TreeItemCollapsibleState.None, undefined, { command: 'extension.setView', title: '', arguments: [element.path, 1] }),
                    new GZoltarCommand('Vertical Partition', vscode.TreeItemCollapsibleState.None, undefined, { command: 'extension.setView', title: '', arguments: [element.path, 2] })
                ];
            }
        }
    }

    refresh(): void {
        this._onDidChangeTreeData.fire(undefined);
    }

    setOption(key: string): void {
        this.reportOptions[key] = !this.reportOptions[key];
        this.refresh();
    }

    setView(path: string, index: number): void {
        const folder = this.container.getFolder(path);
        const webview = folder.getWebview();
        webview?.update(index);
    }

    async reset(key: string, toolspath: string) {
        const folder = this.container.getFolder(key);
        folder.dispose();
        await folder.resetConfig(toolspath);
        vscode.window.showInformationMessage('Reset Completed.');
    }

    async run(key: string) {
        // Sanity check whether Java is available
        let ret = await this.execCmd('javac -version', 'Checking Java version', 'Checking whether Java is available on the command line.');
        if (ret) {
            console.log('bye');
            return;
        }

        // Setup
        vscode.window.showInformationMessage("Setting up all project's artifacts.");
        const folder = this.container.getFolder(key);

        this.statusBar.text = 'GZoltar: Setting up';
        this.statusBar.show();
        let commandRet = await folder.runTests();
        if (commandRet.failed) {
            console.log(commandRet.stdout);
            console.error(commandRet.stderr);
            console.log('bye');
            return;
        }
        await folder.cleanup();
        await folder.copyToBuild();

        const configPath = folder.configPath;
        const includes = await folder.getIncludes();
        const ret_dependencies = await folder.getDependencies();
        let dependencies: string = '';
        if (ret_dependencies instanceof CommandRet) {
            if (ret_dependencies.failed) {
                console.log(ret_dependencies.stdout);
                console.error(ret_dependencies.stderr);
                console.log('bye');
                return;
            }
        } else {
            dependencies = ret_dependencies;
        }
        assert.notStrictEqual(dependencies, '', "Failed to collect project's dependencies");
        const rankingPath = join(configPath, 'sfl', 'txt', 'ochiai.ranking.csv');
        vscode.window.showInformationMessage("Setting all project's artifacts. OK");

        // Get list of test cases
        ret = await this.execCmd(listFunction(configPath, dependencies, folder.testFolder),
            'GZoltar: Collecting test cases', "Collecting project's test cases.");
        if (ret) {
            console.log('bye');
            return;
        }

        // Run test cases
        ret = await this.execCmd(runFunction(configPath, dependencies, includes),
            'GZoltar: Running test cases', "Running project's test cases.");
        if (ret) {
            console.log('bye');
            return;
        }

        // Create fault localization report
        ret = await this.execCmd(reportFunction(configPath, this.reportOptions['Public Methods'], this.reportOptions['Static Constructors'], this.reportOptions['Deprecated Methods']),
            'GZoltar: Generating fault localization report', "Generating fault localization report.");
        if (ret) {
            console.log('bye');
            return;
        }

        // Check whether GZoltar generated the expected ranking file
        if (! fse.existsSync(rankingPath)) {
            vscode.window.showErrorMessage(rankingPath + ' does not exist!');
            return;
        }

        const ranking = (await fse.readFile(rankingPath)).toString();
        folder.setDecorator(Decorator.createDecorator(ranking, this.extensionPath));
        const panel = (await ReportPanel.createPanel(configPath, folder.folderPath));
        panel.onDispose(() => {
            folder.resetWebview();
            this.refresh();
        });
        folder.setWebview(panel);
        this.refresh();

        vscode.window.showInformationMessage('GZoltar has finished');
    }

    private async execCmd(cmd: string, statusBarText: string, windowText: string): Promise<boolean> {
        this.statusBar.text = statusBarText;
        this.statusBar.show();

        vscode.window.showInformationMessage(windowText);
        const ret = await Command.exec(cmd);
        console.log(ret.stdout);
        console.error(ret.stderr);
        if (ret.failed) {
            vscode.window.showErrorMessage(ret.stderr);
            return true;
        }
        vscode.window.showInformationMessage(windowText + ' OK.');

        this.statusBar.hide();
        return false;
    }
}

export class GZoltarCommand extends vscode.TreeItem {

    public children: GZoltarCommand[] = [];

    constructor(
        public readonly label: string,
        public readonly collapsibleState: vscode.TreeItemCollapsibleState,
        public readonly path?: string,
        public command?: vscode.Command
    ) {
        super(label, collapsibleState);
    }

    contextValue = this.path? 'folder' : '';
}
