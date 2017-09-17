package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;

public class MethodAnnotationMatcher extends AbstractMatcher {

  private final String annotation;

  public MethodAnnotationMatcher(final String annotation) {
    this.annotation = annotation;
  }

  @Override
  public final boolean matches(final CtClass c) {
    for (CtMethod m : c.getDeclaredMethods()) {
      if (this.matches(m)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public final boolean matches(final CtBehavior b) {
    try {
      return b.hasAnnotation(Class.forName(annotation));
    } catch (Exception e) {
      return false;
    }
  }
}
