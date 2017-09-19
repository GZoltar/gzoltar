package com.gzoltar.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal utility to parse and create command lines arguments.
 */
public final class CommandLineSupport {

  private static final char BLANK = ' ';
  private static final char QUOTE = '"';
  private static final char SLASH = '\\';

  private static final int M_STRIPWHITESPACE = 0;
  private static final int M_PARSEARGUMENT = 1;
  private static final int M_ESCAPED = 2;

  /**
   * Quotes a single command line argument if necessary.
   * 
   * @param arg command line argument
   * @return quoted argument
   */
  public static String quote(final String arg) {
    final StringBuilder escaped = new StringBuilder();
    for (final char c : arg.toCharArray()) {
      if (c == QUOTE || c == SLASH) {
        escaped.append(SLASH);
      }
      escaped.append(c);
    }
    if (arg.indexOf(BLANK) != -1 || arg.indexOf(QUOTE) != -1) {
      escaped.insert(0, QUOTE).append(QUOTE);
    }
    return escaped.toString();
  }

  /**
   * Builds a single command line string from the given argument list. Arguments are quoted when
   * necessary.
   * 
   * @param args command line arguments
   * @return combined command line
   */
  public static String quote(final List<String> args) {
    final StringBuilder result = new StringBuilder();
    boolean seperate = false;
    for (final String arg : args) {
      if (seperate) {
        result.append(BLANK);
      }
      result.append(quote(arg));
      seperate = true;
    }
    return result.toString();
  }

  /**
   * Splits a command line into single arguments and removes quotes if present.
   * 
   * @param commandline combined command line
   * @return list of arguments
   */
  public static List<String> split(final String commandline) {
    if (commandline == null || commandline.length() == 0) {
      return new ArrayList<String>();
    }
    final List<String> args = new ArrayList<String>();
    final StringBuilder current = new StringBuilder();
    int mode = M_STRIPWHITESPACE;
    int endChar = BLANK;
    for (final char c : commandline.toCharArray()) {
      switch (mode) {
        case M_STRIPWHITESPACE:
          if (!Character.isWhitespace(c)) {
            if (c == QUOTE) {
              endChar = QUOTE;
            } else {
              current.append(c);
              endChar = BLANK;
            }
            mode = M_PARSEARGUMENT;
          }
          break;
        case M_PARSEARGUMENT:
          if (c == endChar) {
            addArgument(args, current);
            mode = M_STRIPWHITESPACE;
          } else if (c == SLASH) {
            current.append(SLASH);
            mode = M_ESCAPED;
          } else {
            current.append(c);
          }
          break;
        case M_ESCAPED:
          if (c == QUOTE || c == SLASH) {
            current.setCharAt(current.length() - 1, c);
          } else if (c == endChar) {
            addArgument(args, current);
            mode = M_STRIPWHITESPACE;
          } else {
            current.append(c);
          }
          mode = M_PARSEARGUMENT;
          break;
        default:
          continue;
      }
    }
    addArgument(args, current);
    return args;
  }

  private static void addArgument(final List<String> args, final StringBuilder current) {
    if (current.length() > 0) {
      args.add(current.toString());
      current.setLength(0);
    }
  }
}
