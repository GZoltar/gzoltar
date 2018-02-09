package com.gzoltar.cli;

import org.kohsuke.args4j.CmdLineParser;

public class CommandParser extends CmdLineParser {

  private final Command command;

  public CommandParser(final Command command) {
    super(command);
    this.command = command;
  }

  public Command getCommand() {
    return command;
  }
}
