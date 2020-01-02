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
package com.gzoltar.cli.commands;

import java.util.Arrays;
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
    return Arrays.asList(new Version(), new ListTestMethods(), new Instrument(),
        new RunTestMethods(), new FaultLocalizationReport());
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
