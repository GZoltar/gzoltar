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
    throw new RuntimeException(ClassAnnotationMatcher.class.getSimpleName()
        + " should only be used to filter out classes");
  }

  @Override
  public final boolean matches(final CtField ctField) {
    throw new RuntimeException(ClassAnnotationMatcher.class.getSimpleName()
        + " should only be used to filter out classes");
  }

}
