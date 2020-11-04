'use strict';

import * as vscode from 'vscode';
import { join } from 'path';
import { FolderContainer } from './workspace/container';
import { GZoltarCommander, GZoltarCommand } from './commander';

export async function activate(context: vscode.ExtensionContext) {

	if (!vscode.workspace.workspaceFolders || vscode.workspace.workspaceFolders.length === 0) {
		throw new Error('Unable to locate workspace, extension has been activated incorrectly.');
	}

	const toolsPath = join(context.extensionPath, 'tools');
	const folders = vscode.workspace.workspaceFolders.map(wf => wf.uri.fsPath);
	const container = new FolderContainer(toolsPath, folders);
	const commander = new GZoltarCommander(context.extensionPath, container);

	context.subscriptions.push(vscode.workspace.onDidChangeWorkspaceFolders(async (ev) => {
		const addedFolders = ev.added.map(wf => wf.uri.fsPath);
		const removedFolders = ev.removed.map(wf => wf.uri.fsPath);
		await container.updateFolders(addedFolders, removedFolders);
		commander.refresh();
	}));

	vscode.window.registerTreeDataProvider('gzoltar', commander);
	vscode.commands.registerCommand('gzoltar.refresh', () => commander.refresh());
	vscode.commands.registerCommand('extension.setOption', (arg) => commander.setOption(arg));
	vscode.commands.registerCommand('extension.setView', (path, index) => commander.setView(path, index));
	vscode.commands.registerCommand('gzoltar.reset', async (cmd: GZoltarCommand) => await commander.reset(cmd.path!, toolsPath));
	vscode.commands.registerCommand('gzoltar.run', async (cmd: GZoltarCommand) => await commander.run(cmd.path!));
}

export function deactivate() { }
