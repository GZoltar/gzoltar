package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class JUnitMatcher implements IMatcher {

  private IMatcher matcher;

  public JUnitMatcher() {
    this.matcher = new OrMatcher(new SuperclassMatcher("junit.framework.TestCase"),
        new MethodAnnotationMatcher("org.junit.Test"),
        new MethodAnnotationMatcher("org.junit.experimental.theories.Theory"));
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    return this.matcher.matches(ctClass);
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return this.matcher.matches(ctBehavior);
  }

  @Override
  public boolean matches(final CtField ctField) {
    return false;
  }
}
