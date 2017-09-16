package com.gzoltar.core.instr.matchers;

import java.util.regex.Pattern;
import javassist.CtClass;
import javassist.CtMethod;

public class MethodNameMatcher extends AbstractMatcher {

  private final Pattern pattern;

  public MethodNameMatcher(final String expression) {
    this.pattern = super.matches(expression);
  }

  @Override
  public final boolean matches(final CtClass c) {
    return false;
  }

  @Override
  public final boolean matches(final CtMethod m) {
    return this.pattern.matcher(m.getName()).matches();
  }

}
