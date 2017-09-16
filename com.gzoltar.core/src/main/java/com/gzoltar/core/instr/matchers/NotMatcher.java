package com.gzoltar.core.instr.matchers;

import javassist.CtClass;
import javassist.CtMethod;

public class NotMatcher extends AbstractMatcher {

  private final IMatcher matcher;

  public NotMatcher(final IMatcher matcher) {
    this.matcher = matcher;
  }

  @Override
  public final boolean matches(final CtClass c) {
    return !this.matcher.matches(c);
  }

  @Override
  public final boolean matches(final CtMethod m) {
    return !this.matcher.matches(m);
  }

}
