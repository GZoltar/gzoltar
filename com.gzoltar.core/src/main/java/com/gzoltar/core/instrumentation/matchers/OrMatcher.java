package com.gzoltar.core.instrumentation.matchers;

import java.security.ProtectionDomain;
import javassist.CtClass;
import javassist.CtMethod;

public class OrMatcher implements Matcher {
  private final Matcher[] matchers;

  public OrMatcher(Matcher... matchers) {
    this.matchers = matchers;
  }

  @Override
  public final boolean matches(CtClass c, ProtectionDomain d) {
    for (Matcher mat : matchers) {
      if (mat.matches(c, d))
        return true;
    }

    return false;
  }

  @Override
  public final boolean matches(CtClass c, CtMethod m) {
    for (Matcher mat : matchers) {
      if (mat.matches(c, m))
        return true;
    }

    return false;
  }
}
