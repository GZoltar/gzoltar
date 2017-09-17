package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;

public interface IMatcher {

  public boolean matches(final CtClass c);

  public boolean matches(final CtBehavior b);
}
