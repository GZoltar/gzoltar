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
import { join, sep } from 'path';
import { disassemble } from './cmdLine/cmdBuilder';
import { Command, CommandRet } from './cmdLine/command';

export class ReportPanel {

    private static readonly viewType = 'report';

    private static readonly fileReg = /(.+)\$(.+)\#(.+)\:(.+)/;
    private static readonly classReg = /SourceFile: "(.+)"/;

    private readonly panel: vscode.WebviewPanel;
    private readonly configPath: string;
    private readonly workspacePath: string;
    private readonly views: string[];
    private disposables: vscode.Disposable[] = [];
    private disposeListener?: () => any;

    private constructor(panel: vscode.WebviewPanel, configPath: string, workspacePath: string, views: string[]) {
        this.panel = panel;
        this.configPath = configPath;
        this.workspacePath = workspacePath;
        this.views = views;
        this.update(0);
        this.panel.onDidDispose(() => this.dispose(), null, this.disposables);
        const lstnr = this.panel.webview.onDidReceiveMessage((message) => this.openDoc(message.label));
        this.disposables.push(lstnr);
    }
    
    public update(index: number): void {
        this.panel.webview.html = this.views[index];
        this.panel.reveal();
    }

    public onDispose(listener: () => any): void {
        this.disposeListener = listener;
    }

    public dispose(): void {
        this.panel.dispose();
        this.disposeListener?.();
        this.disposables.forEach(d => d?.dispose());
        this.disposables = [];
    }

    private async openDoc(label: string): Promise<void> {
        const fSplit = String(label).split(ReportPanel.fileReg);
        const packageName = fSplit[1].replace(/\./g, sep);
        const file = join(packageName, fSplit[2]);

        const classFile = join('build', file) + '.class';
        const dissassemble = await Command.exec(disassemble(this.configPath, classFile));
        if (dissassemble.failed) {
            throw dissassemble.stderr;
        }
        const dSplit = dissassemble.stdout.split(ReportPanel.classReg);
        
        const filename = join(this.workspacePath, 'src', 'main', 'java', packageName, dSplit[1]);
        vscode.window.showTextDocument(vscode.Uri.file(filename), { viewColumn: vscode.ViewColumn.One });
    }

    public static async createPanel(configPath: string, workspacePath: string): Promise<ReportPanel> {
        const panel = vscode.window.createWebviewPanel(
            ReportPanel.viewType,
            'GZoltar Reports',
            vscode.ViewColumn.Beside,
            {
                enableScripts: true,
                localResourceRoots: [ vscode.Uri.file(join(configPath, 'sfl')) ]
            }
        );

        const viewPath = join(configPath, 'sfl', 'html', 'ochiai');
        const scriptPathOnDisk = vscode.Uri.file(join(viewPath, "gzoltar.js"));
        const scriptUri = panel.webview.asWebviewUri(scriptPathOnDisk);

        const views = await Promise.all(
            ['sunburst.html', 'bubblehierarchy.html', 'verticalpartition.html']
                .map(s => this.setScript(join(viewPath, s), scriptUri.toString())));

        return new ReportPanel(panel, configPath, workspacePath, views);
    }

    private static async setScript(filename: string, script: string): Promise<string> {
        const file = (await fse.readFile(filename)).toString();
        const newHtml = file.replace(
            '<script type="text/javascript" src="gzoltar.js"></script>',
            `<script type="text/javascript" src="${script}"></script>`);
        return newHtml;
    }
}