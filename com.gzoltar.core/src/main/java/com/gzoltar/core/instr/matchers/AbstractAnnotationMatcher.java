package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public abstract class AbstractAnnotationMatcher implements IMatcher {

  private final String annotation;

  protected AbstractAnnotationMatcher(final String annotation) {
    this.annotation = annotation;
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    return ctClass.hasAnnotation(this.annotation);
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return ctBehavior.hasAnnotation(this.annotation);
  }

  @Override
  public boolean matches(final CtField ctField) {
    return ctField.hasAnnotation(this.annotation);
  }

}
