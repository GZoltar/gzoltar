package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class ClassAnnotationMatcher extends AbstractAnnotationMatcher {

  public ClassAnnotationMatcher(final String annotation) {
    super(annotation);
  }

  @Override
  public final boolean matches(final CtClass ctClass) {
    return super.matches(ctClass);
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
