package com.gzoltar.core.instr.matchers;

import org.jacoco.core.runtime.WildcardMatcher;

public abstract class AbstractWildcardMatcher extends WildcardMatcher implements IMatcher {

  /**
   * {@inheritDoc}
   */
  public AbstractWildcardMatcher(final String expression) {
    super(expression);
  }

}
