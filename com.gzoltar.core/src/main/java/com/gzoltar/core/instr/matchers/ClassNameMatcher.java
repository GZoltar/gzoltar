package com.gzoltar.core.instr.matchers;

import java.util.regex.Pattern;
import javassist.CtBehavior;
import javassist.CtClass;

public class ClassNameMatcher extends AbstractMatcher {

  private final Pattern pattern;

  public ClassNameMatcher(final String expression) {
    this.pattern = super.matches(expression);
  }

  @Override
  public final boolean matches(final CtClass c) {
    return this.pattern.matcher(c.getName()).matches();
  }

  @Override
  public final boolean matches(final CtBehavior b) {
    return false;
  }
}
