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
