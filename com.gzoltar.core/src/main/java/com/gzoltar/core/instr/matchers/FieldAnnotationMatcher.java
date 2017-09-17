package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class FieldAnnotationMatcher extends AbstractAnnotationMatcher {

  public FieldAnnotationMatcher(final String annotation) {
    super(annotation);
  }

  @Override
  public final boolean matches(final CtClass ctClass) {
    for (CtField ctField : ctClass.getDeclaredFields()) {
      if (this.matches(ctField)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public final boolean matches(final CtBehavior ctBehavior) {
    return this.matches(ctBehavior.getDeclaringClass());
  }

  @Override
  public final boolean matches(final CtField ctField) {
    return super.matches(ctField);
  }

}
