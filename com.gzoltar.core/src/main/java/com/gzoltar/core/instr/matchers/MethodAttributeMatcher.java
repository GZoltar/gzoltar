package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class MethodAttributeMatcher extends AbstractAttributeMatcher {

  public MethodAttributeMatcher(final String attribute) {
    super(attribute);
  }

  @Override
  public final boolean matches(final CtClass ctClass) {
    for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
      if (this.matches(ctBehavior)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public final boolean matches(final CtBehavior ctBehavior) {
    return super.matches(ctBehavior);
  }

  @Override
  public final boolean matches(final CtField ctField) {
    throw new RuntimeException(MethodAttributeMatcher.class.getSimpleName()
        + " should only be used to filter out methods or classes with methods");
  }

}
