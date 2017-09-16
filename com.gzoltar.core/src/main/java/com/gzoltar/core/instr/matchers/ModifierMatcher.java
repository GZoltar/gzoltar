package com.gzoltar.core.instr.matchers;

import javassist.CtClass;
import javassist.CtMethod;

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
  public final boolean matches(final CtMethod m) {
    return this.matches(m.getModifiers());
  }

  private boolean matches(final int modifier) {
    return (modifier & this.modifierMask) != 0;
  }
}
