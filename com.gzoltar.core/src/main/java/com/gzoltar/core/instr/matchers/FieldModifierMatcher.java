package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class FieldModifierMatcher extends AbstractModifierMatcher {

  public FieldModifierMatcher(final int modifierMask) {
    super(modifierMask);
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    for (CtField ctField : ctClass.getDeclaredFields()) {
      if (this.matches(ctField)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return this.matches(ctBehavior.getDeclaringClass());
  }

  @Override
  public boolean matches(final CtField ctField) {
    return super.matches(ctField);
  }

}
