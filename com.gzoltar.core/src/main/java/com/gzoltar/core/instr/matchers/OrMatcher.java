package com.gzoltar.core.instr.matchers;

import javassist.CtClass;
import javassist.CtMethod;

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
  public final boolean matches(final CtMethod m) {
    for (IMatcher mat : this.matchers) {
      if (mat.matches(m)) {
        return true;
      }
    }
    return false;
  }

}
