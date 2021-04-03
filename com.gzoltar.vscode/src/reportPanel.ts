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
        console.log("[gz] label: " + label); // mypackage$App$Foo#mid(int,int,int):13

        let packageName = label.substring(0, label.indexOf('$'));
        packageName = packageName.replace(/\./g, sep);
        console.log(packageName);

        let className = label.substring(label.indexOf('$') + 1, label.indexOf('#'));
        if (className.indexOf('$') != -1) {
            className = className.substring(0, className.indexOf('$'));
        }
        console.log(className);

        let methodName = label.substring(label.indexOf('#') + 1, label.indexOf(':'));
        console.log(methodName);

        let lineNumber = parseInt(label.substring(label.indexOf(':') + 1));
        console.log(lineNumber);

        // FIXME the following line only works for Maven-based projects
        const javaFile = join(this.workspacePath, 'src', 'main', 'java', packageName, className + '.java');
        console.log(javaFile);

        vscode.window.showTextDocument(vscode.Uri.file(javaFile), { viewColumn: vscode.ViewColumn.One }).then(textEditor => {
            const line = lineNumber - 1;
            // TODO replace the hardcoded column number (i.e., 1000) with the real length of line of code
            const column = 1_000;
            const selection = new vscode.Selection(
                new vscode.Position(line, column),
                new vscode.Position(line, column)
            );
            textEditor.selection = selection;
        });
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