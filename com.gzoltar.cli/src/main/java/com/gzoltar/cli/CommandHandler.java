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
