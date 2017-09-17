package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public abstract class AbstractAttributeMatcher implements IMatcher {

  private final String attribute;

  protected AbstractAttributeMatcher(final String attribute) {
    this.attribute = attribute;
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    return ctClass.getAttribute(this.attribute) != null;
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return ctBehavior.getAttribute(this.attribute) != null;
  }

  @Override
  public boolean matches(final CtField ctField) {
    return ctField.getAttribute(this.attribute) != null;
  }

}
