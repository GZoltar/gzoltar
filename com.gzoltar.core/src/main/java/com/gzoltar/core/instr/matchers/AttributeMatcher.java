package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;

public class AttributeMatcher extends AbstractMatcher {

  private final String attribute;

  public AttributeMatcher(final String attribute) {
    this.attribute = attribute;
  }

  @Override
  public final boolean matches(final CtClass c) {
    return c.getAttribute(this.attribute) != null;
  }

  @Override
  public final boolean matches(final CtBehavior b) {
    return b.getAttribute(this.attribute) != null;
  }

}
