package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;

public class OrMatcher extends AbstractMatcher {

  private final IMatcher[] matchers;

  public OrMatcher(final IMatcher... matchers) {
    this.matchers = matchers;
  }

  @Override
  public final boolean matches(final CtClass c) {
    for (IMatcher mat : this.matchers) {
      if (mat.matches(c)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public final boolean matches(final CtBehavior b) {
    for (IMatcher mat : this.matchers) {
      if (mat.matches(b)) {
        return true;
      }
    }
    return false;
  }

}
