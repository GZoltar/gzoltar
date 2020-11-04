'use strict';

import * as vscode from 'vscode';
import * as fse from 'fs-extra';
import { join, basename } from 'path';
import { listFunction, runFunction, reportFunction } from './cmdLine/cmdBuilder';
import { ReportPanel } from './reportPanel';
import { Decorator } from './decoration/decorator';
import { FolderContainer } from './workspace/container';
const exec = require('util').promisify(require('child_process').exec);

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
        vscode.window.showInformationMessage('Run Initiated.');
        const folder = this.container.getFolder(key);

        this.statusBar.text = 'GZoltar: Setting up';
        this.statusBar.show();
        await folder.runTests().catch(_ => { });
        await folder.cleanup();
        await folder.copyToBuild();
        
        const configPath = folder.configPath;
        const includes = await folder.getIncludes();
        const dependencies = await folder.getDependencies();
        const rankingPath = join(configPath, 'sfl', 'txt', 'ochiai.ranking.csv');

        await this.execCmd(listFunction(configPath, dependencies, folder.testFolder), 'GZoltar: Listing Test Methods');
        await this.execCmd(runFunction(configPath, dependencies, includes), 'GZoltar: Running Test Methods');
        await this.execCmd(reportFunction(configPath, this.reportOptions['Public Methods'], this.reportOptions['Static Constructors'], this.reportOptions['Deprecated Methods']), 'GZoltar: Generating Report');

        const ranking = (await fse.readFile(rankingPath)).toString();
        folder.setDecorator(Decorator.createDecorator(ranking, this.extensionPath));
        const panel = (await ReportPanel.createPanel(configPath, folder.folderPath));
        panel.onDispose(() => {
            folder.resetWebview();
            this.refresh();
        });
        folder.setWebview(panel);
        this.refresh();
    }

    private async execCmd(cmd: string, text: string): Promise<void> {
        this.statusBar.text = text;
        this.statusBar.show();
        await exec(cmd);
        this.statusBar.hide();
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
