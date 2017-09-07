package com.gzoltar.core.instrumentation.matchers;

import java.security.ProtectionDomain;
import javassist.CtClass;
import javassist.CtMethod;

public abstract class AbstractActionTaker implements ActionTaker {
  private final Matcher matcher;


  public AbstractActionTaker(Matcher matcher) {
    this.matcher = matcher;
  }

  @Override
  public final Action getAction(CtClass c, ProtectionDomain d) {
    return getAction(matcher.matches(c, d));
  }

  @Override
  public final Action getAction(CtClass c, CtMethod m) {
    return getAction(matcher.matches(c, m));
  }

  protected abstract Action getAction(boolean matches);
}
