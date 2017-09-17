package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class AndMatcher implements IMatcher {

  private final IMatcher[] matchers;

  public AndMatcher(final IMatcher... matchers) {
    this.matchers = matchers;
  }

  @Override
  public final boolean matches(final CtClass ctClass) {
    for (IMatcher mat : this.matchers) {
      if (!mat.matches(ctClass)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public final boolean matches(final CtBehavior ctBehavior) {
    for (IMatcher mat : this.matchers) {
      if (!mat.matches(ctBehavior)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public final boolean matches(final CtField ctField) {
    for (IMatcher mat : this.matchers) {
      if (!mat.matches(ctField)) {
        return false;
      }
    }
    return true;
  }

}
