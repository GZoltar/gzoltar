package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public abstract class AbstractModifierMatcher implements IMatcher {

  private final int modifierMask;

  protected AbstractModifierMatcher(final int modifierMask) {
    this.modifierMask = modifierMask;
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    return this.matches(ctClass.getModifiers());
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return this.matches(ctBehavior.getMethodInfo().getAccessFlags());
  }

  @Override
  public boolean matches(final CtField ctField) {
    return this.matches(ctField.getFieldInfo().getAccessFlags());
  }

  private final boolean matches(final int modifier) {
    return (modifier & this.modifierMask) != 0;
  }

}
