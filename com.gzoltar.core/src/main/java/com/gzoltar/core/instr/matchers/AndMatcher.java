package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;

public class AndMatcher extends AbstractMatcher {

  private final IMatcher[] matchers;

  public AndMatcher(final IMatcher... matchers) {
    this.matchers = matchers;
  }

  @Override
  public final boolean matches(final CtClass c) {
    for (IMatcher mat : this.matchers) {
      if (!mat.matches(c)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public final boolean matches(final CtBehavior b) {
    for (IMatcher mat : this.matchers) {
      if (!mat.matches(b)) {
        return false;
      }
    }
    return true;
  }
}
