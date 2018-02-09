package com.gzoltar.cli.commands;

import java.util.ArrayList;
import java.util.List;
import com.gzoltar.cli.Command;

/**
 * List of all available commands.
 */
public final class Commands {

  private Commands() {
    // no-op
  }

  /**
   * @return list of new instances of all available commands
   */
  public static List<Command> get() {
    List<Command> allCommands = new ArrayList<Command>();
    allCommands.add(new Version());
    return allCommands;
  }

  /**
   * @return String containing all available command names
   */
  public static String names() {
    final StringBuilder sb = new StringBuilder();
    for (final Command c : get()) {
      if (sb.length() > 0) {
        sb.append('|');
      }
      sb.append(c.name());
    }
    return sb.toString();
  }

}
