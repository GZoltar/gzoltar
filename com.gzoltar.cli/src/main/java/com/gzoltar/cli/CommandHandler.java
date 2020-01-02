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
package com.gzoltar.cli;

import java.util.AbstractList;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Messages;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;
import com.gzoltar.cli.commands.Commands;

/**
 * {@link org.kohsuke.args4j.spi.OptionHandler} which uses {@link com.gzoltar.cli.CommandParser}
 * internally to provide help context also for sub-commands.
 * 
 * DISCLAIMER: this class has been exported from JaCoCo's cli module for convenience.
 */
public class CommandHandler extends OptionHandler<Command> {

  /**
   * This constructor is required by the args4j framework.
   * 
   * @param parser
   * @param option
   * @param setter
   */
  public CommandHandler(final CmdLineParser parser, final OptionDef option,
      final Setter<Object> setter) {
    super(parser, new OptionDef(Commands.names(), "<command>", option.required(), option.help(),
        option.hidden(), CommandHandler.class, option.isMultiValued()) {}, setter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int parseArguments(final Parameters params) throws CmdLineException {
    final String subCmd = params.getParameter(0);

    for (final Command command : Commands.get()) {
      if (command.name().equals(subCmd)) {
        parseSubArguments(command, params);
        setter.addValue(command);
        return params.size(); // consume all the remaining tokens
      }
    }

    throw new CmdLineException(owner, Messages.ILLEGAL_OPERAND, subCmd);
  }

  private void parseSubArguments(final Command c, final Parameters params) throws CmdLineException {
    final CmdLineParser p = new CommandParser(c);
    p.parseArgument(new AbstractList<String>() {
      @Override
      public String get(final int index) {
        try {
          return params.getParameter(index + 1);
        } catch (final CmdLineException e) {
          // invalid index was accessed.
          throw new IndexOutOfBoundsException();
        }
      }

      @Override
      public int size() {
        return params.size() - 1;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefaultMetaVariable() {
    return "<command>";
  }
}
