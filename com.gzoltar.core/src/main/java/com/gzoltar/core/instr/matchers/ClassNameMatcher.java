package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class ClassNameMatcher extends AbstractWildcardMatcher {

  public ClassNameMatcher(final String expression) {
    super(expression);
  }

  @Override
  public final boolean matches(final CtClass ctClass) {
    return super.matches(ctClass.getName());
  }

  @Override
  public final boolean matches(final CtBehavior ctBehavior) {
    return this.matches(ctBehavior.getDeclaringClass());
  }

  @Override
  public final boolean matches(final CtField ctField) {
    return this.matches(ctField.getDeclaringClass());
  }

}
