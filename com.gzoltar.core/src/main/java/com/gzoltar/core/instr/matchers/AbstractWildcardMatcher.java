package com.gzoltar.core.instr.matchers;

import java.util.regex.Pattern;

public abstract class AbstractWildcardMatcher implements IMatcher {

  private final Pattern pattern;

  /**
   * Matches strings against <code>?</code>/<code>*</code> wildcard expressions. Multiple
   * expressions can be separated with a colon (:). In this case the expression matches if at least
   * one part matches.
   */
  protected AbstractWildcardMatcher(final String expression) {
    final String[] parts = expression.split("\\:");
    final StringBuilder regex = new StringBuilder(expression.length() * 2);
    boolean next = false;
    for (final String part : parts) {
      if (next) {
        regex.append('|');
      }
      regex.append('(').append(toRegex(part)).append(')');
      next = true;
    }
    this.pattern = Pattern.compile(regex.toString());
  }

  private static CharSequence toRegex(final String expression) {
    final StringBuilder regex = new StringBuilder(expression.length() * 2);
    for (final char c : expression.toCharArray()) {
      switch (c) {
        case '?':
          regex.append(".?");
          break;
        case '*':
          regex.append(".*");
          break;
        default:
          regex.append(Pattern.quote(String.valueOf(c)));
          break;
      }
    }
    return regex;
  }

  protected boolean matches(final String s) {
    return this.pattern.matcher(s).matches();
  }

}
