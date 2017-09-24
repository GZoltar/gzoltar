package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class ClassModifierMatcher extends AbstractModifierMatcher {

  public ClassModifierMatcher(final int modifierMask) {
    super(modifierMask);
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    return super.matches(ctClass);
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return this.matches(ctBehavior.getDeclaringClass());
  }

  @Override
  public boolean matches(final CtField ctField) {
    return this.matches(ctField.getDeclaringClass());
  }

}
