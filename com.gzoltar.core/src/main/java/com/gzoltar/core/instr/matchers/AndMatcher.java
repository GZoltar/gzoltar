package com.gzoltar.core.instr.matchers;

import javassist.CtClass;
import javassist.CtMethod;

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
  public final boolean matches(final CtMethod m) {
    for (IMatcher mat : this.matchers) {
      if (!mat.matches(m)) {
        return false;
      }
    }
    return true;
  }
}
