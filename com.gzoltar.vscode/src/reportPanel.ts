'use strict';

import * as vscode from 'vscode';
import * as fse from 'fs-extra';
import { join, sep } from 'path';
import { disassemble } from './cmdLine/cmdBuilder';
const exec = require('util').promisify(require('child_process').exec);

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
        const dissassemble = await exec(disassemble(this.configPath, classFile));
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