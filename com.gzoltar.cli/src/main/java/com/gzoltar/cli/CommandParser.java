package com.gzoltar.cli;

import org.kohsuke.args4j.CmdLineParser;

/**
 * Parser which remembers the parsed command to have additional context information to produce help
 * output.
 * 
 * DISCLAIMER: this class has been exported from JaCoCo's cli module for convenience.
 */
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
