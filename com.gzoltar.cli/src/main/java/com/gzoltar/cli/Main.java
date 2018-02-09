package com.gzoltar.cli;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;

public class Main extends Command {

  /**
   * Entry point of GZoltar execution
   * 
   * @param args
   */
  public static void main(final String[] args) throws Exception {
    final PrintWriter out = new PrintWriter(System.out, true);
    final PrintWriter err = new PrintWriter(System.err, true);
    final int returncode = new Main(args).execute(out, err);
    System.exit(returncode);
  }

  private final String[] args;

  private Main(final String[] args) {
    this.args = args;
  }

  @Argument(handler = CommandHandler.class, required = true)
  private Command command;

  /**
   * {@inheritDoc}
   */
  @Override
  public String description() {
    return "Command line interface for GZoltar.";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String usage(final CommandParser parser) {
    return JAVACMD + "--help | <command>";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int execute(PrintWriter out, final PrintWriter err) throws Exception {

    out.println("   ____ _____     _ _               \n" +
                "  / ___|__  /___ | | |_ __ _ _ __   \n" +
                " | |  _  / // _ \\| | __/ _` | '__| \n" +
                " | |_| |/ /| (_) | | || (_| | |     \n" +
                "  \\____/____\\___/|_|\\__\\__,_|_| \n");

    final CommandParser mainParser = new CommandParser(this);
    try {
      mainParser.parseArgument(this.args);
    } catch (final CmdLineException e) {
      ((CommandParser) e.getParser()).getCommand().printHelp(err);
      err.println();
      err.println(e.getMessage());
      return -1;
    }

    if (this.help) {
      printHelp(out);
      return 0;
    }

    if (this.command.help) {
      this.command.printHelp(out);
      return 0;
    }

    if (this.command.quiet) {
      out = NUL;
    }

    return this.command.execute(out, err);
  }

  private static final PrintWriter NUL = new PrintWriter(new Writer() {
    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final char[] arg0, final int arg1, final int arg2) throws IOException {
      // no-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws IOException {
      // no-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
      // no-op
    }
  });
}
