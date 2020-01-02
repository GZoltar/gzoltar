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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;

/**
 * Entry point for all command line operations.
 * 
 * DISCLAIMER: this class has been exported from JaCoCo's cli module for convenience.
 */
public class Main extends Command {

  /**
   * Entry point of GZoltar execution
   * 
   * @param args
   */
  public static void main(final String[] args) throws Exception {
    final PrintStream out = new PrintStream(System.out, true);
    final PrintStream err = new PrintStream(System.err, true);
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
  public int execute(PrintStream out, final PrintStream err) throws Exception {

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

    out.println("   ____ _____     _ _               \n" +
                "  / ___|__  /___ | | |_ __ _ _ __   \n" +
                " | |  _  / // _ \\| | __/ _` | '__| \n" +
                " | |_| |/ /| (_) | | || (_| | |     \n" +
                "  \\____/____\\___/|_|\\__\\__,_|_| \n");

    return this.command.execute(out, err);
  }

  private static final PrintStream NUL = new PrintStream(new OutputStream() {
    @Override
    public void write(int b) throws IOException {
      // no-op
    }

    @Override
    public void write(byte[] b) throws IOException {
      // no-op
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      // no-op
    }

    @Override
    public void flush() throws IOException {
      // no-op
    }

    @Override
    public void close() throws IOException {
      // no-op
    }
  });
}
