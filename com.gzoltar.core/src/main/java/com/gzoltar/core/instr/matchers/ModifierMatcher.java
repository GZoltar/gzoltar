package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;

public class ModifierMatcher extends AbstractMatcher {

  private final int modifierMask;

  public ModifierMatcher(final int modifierMask) {
    this.modifierMask = modifierMask;
  }

  @Override
  public final boolean matches(final CtClass c) {
    return this.matches(c.getModifiers());
  }

  @Override
  public final boolean matches(final CtBehavior b) {
    return this.matches(b.getMethodInfo().getAccessFlags());
  }

  private boolean matches(final int modifier) {
    return (modifier & this.modifierMask) != 0;
  }
}
