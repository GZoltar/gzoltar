package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class OrMatcher implements IMatcher {

  private final IMatcher[] matchers;

  public OrMatcher(final IMatcher... matchers) {
    this.matchers = matchers;
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    for (IMatcher mat : this.matchers) {
      if (mat.matches(ctClass)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    for (IMatcher mat : this.matchers) {
      if (mat.matches(ctBehavior)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean matches(final CtField ctField) {
    for (IMatcher mat : this.matchers) {
      if (mat.matches(ctField)) {
        return true;
      }
    }
    return false;
  }

}
