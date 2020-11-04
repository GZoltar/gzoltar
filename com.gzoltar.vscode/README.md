# GZoltar for Visual Studio Code

GZoltar is a framework for automatic testing and fault localization for Java projects. It integrates seamlessly with JUnit tests, and provides intuitive feedback about code faults by using different visualization techniques.

## Overview

* [Introduction](#introduction)
* [Getting Started](#getting-started)
* [Features](#features)
* [Compiling and Publishing](#compiling-and-publishing)
* [Questions and Feedback](#questions-and-feedback)

## Introduction

GZoltar is a graphic debugger which can be used to identify faults in a Java project. By making use of hierarchical visualizations and colors to indicate suspicion levels, it indicates which parts of the project are most likely to contain the fault verified in a test suite. After running GZoltar on a project, you are able to analyze three charts to better understand where the fault might be.

## Getting Started

Before using GZoltar, a few requirements have to be met in order to use its capabilities to the fullest. Follow the list of requirements below before using the extension.

### JDK

Make sure you have at least Java 8 installed in your computer, since GZoltar executes fault localization on Java projects.

### Build Tool

This extension will only be activated on projects containing either of the following build tools: maven or gradle. The project under test must contain either a pom.xml or build.gradle file.

### Environment

Linux and MacOS users can immediately start using the extension if the previous requirements are already met. Windows users however, must take some additional steps. This is due to an underlying fault in GZoltar which causes the execution to produce incorrect results.

First step is to install a Windows subsystem for Linux of your choosing. As long as you are able to use a Linux terminal in your computer, it should be good to go. The next step is to install the extension [Remote - WSL](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-wsl). This extension allows you to use Visual Studio Code to build Linux applications that run on the Windows Subsystem for Linux. After installing this extension, open a new window of VS Code and click on the green arrows in the bottom left corner to open a remote window.

![Remote Dev Status Bar](https://github.com/Klaysb/vscode-gzoltar/blob/master/resources/docs/remote-dev-status-bar.png?raw=true)

Now, you are able to start working with GZoltar just like you would with a regular VS Code window.

## Features

To open GZoltar's menu, click on its icon in the activity bar.

![GZoltar Activity Bar](https://github.com/Klaysb/vscode-gzoltar/blob/master/resources/docs/barui.png?raw=true)

To run GZoltar on a project that is currently in your workspace, click on the icon next to the project's name.

![GZoltar Commands](https://github.com/Klaysb/vscode-gzoltar/blob/master/resources/docs/gcmds.png?raw=true)

While GZoltar is running, you are able to see on the lower right corner of the status bar what phase it is currently on.

![GZoltar Status Bar](https://github.com/Klaysb/vscode-gzoltar/blob/master/resources/docs/statusbar.png?raw=true)

After it's finished, a new window will appear showcasing the results of the fault detection analysis. The colors in the charts indicate the code's suspicion levels. Green means the segment has no suspicion, yellow is for medium likelihood, orange for high likelihood and red for very high likelihood. The further you zoom in into the chart, the finer grained it becomes. Meaning that at the beginning you are viewing the project in its entirety (at the root level), and at the deepest level you are viewing a single line of code.

![GZoltar Results](https://github.com/Klaysb/vscode-gzoltar/blob/master/resources/docs/result.png?raw=true)

It is possible to navigate through the chart by double clicking on each segment. A right click will reset the chart back to its initial state. Clicking on an edge segment (which corresponds to a single line of code) will open the file associated with that line of code.

You can also change the visualization that is currently being shown. On the activity bar menu, under the project that was just used to perform an analysis, you can choose one of the three charts to change the visualization to better suit your taste.

![GZoltar Options](https://github.com/Klaysb/vscode-gzoltar/blob/master/resources/docs/options.png?raw=true)

When a file is opened after performing an analysis, it will show an icon to indicate the level of suspiciousness for each line of code. The icon's colors are the same as the ones represented in the charts.

![GZoltar Open File](https://github.com/Klaysb/vscode-gzoltar/blob/master/resources/docs/openfile.png?raw=true)

## Compiling and Publishing

Before compiling and attempting to work directly on the extension, ensure the following files are on the `tools` folder:

```
gzoltaragent.jar
gzoltarcli.jar
hamcrest-core-2.2.jar
junit-4.13.jar
```

To compile the extension, run the command:

```
$ npm run compile
```

To package the extension into a VSIX file, run the command:

```
$ vsce package
```

There are two possible ways to publish the extension. Assuming you are a member of the GZoltar Publisher, you can obtain a Personal Access Token (PAT) in https://dev.azure.com. Then, you can use the command:

```
$ vsce login GZoltar
Personal Access Token for publisher 'GZoltar': <insert your PAT>
$ vsce publish
```

Alternatively, you can do both steps in one with the following command:

```
$ vsce publish -p <token>
```

## Questions and Feedback

If you are having trouble using this extension, feel free to ask us any questions you might have or any suggestions you would like to see. All feedback is appreciated.