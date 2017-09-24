package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class NotMatcher implements IMatcher {

  private final IMatcher matcher;

  public NotMatcher(final IMatcher matcher) {
    this.matcher = matcher;
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    return !this.matcher.matches(ctClass);
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return !this.matcher.matches(ctBehavior);
  }

  @Override
  public boolean matches(final CtField ctField) {
    return !this.matcher.matches(ctField);
  }

}
