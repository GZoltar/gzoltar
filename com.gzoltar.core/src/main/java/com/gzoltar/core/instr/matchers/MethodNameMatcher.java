package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.Descriptor;

public class MethodNameMatcher extends AbstractWildcardMatcher {

  public MethodNameMatcher(final String expression) {
    super(expression);
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    for (CtBehavior ctBehavior : ctClass.getMethods()) {
      if (this.matches(ctBehavior)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return super.matches(ctBehavior.getName() + Descriptor.toString(ctBehavior.getSignature()));
  }

  @Override
  public boolean matches(final CtField ctField) {
    return this.matches(ctField.getDeclaringClass());
  }

}
