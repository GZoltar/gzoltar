package com.gzoltar.core.instr.matchers;

import javassist.CtClass;
import javassist.CtMethod;

public interface IMatcher {

  public boolean matches(final CtClass c);

  public boolean matches(final CtMethod m);
}
