package com.gzoltar.core.instrumentation.matchers;

import java.security.ProtectionDomain;
import javassist.CtClass;
import javassist.CtMethod;


public class ModifierMatcher implements Matcher {
  private final int modifierMask;

  public ModifierMatcher(int modifierMask) {
    this.modifierMask = modifierMask;
  }

  @Override
  public final boolean matches(CtClass c, ProtectionDomain d) {
    return matches(c.getModifiers());
  }

  @Override
  public final boolean matches(CtClass c, CtMethod m) {
    return matches(m.getModifiers());
  }

  private boolean matches(int modifier) {
    return (modifier & modifierMask) != 0;
  }
}
