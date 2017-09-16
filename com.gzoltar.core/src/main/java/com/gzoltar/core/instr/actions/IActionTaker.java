package com.gzoltar.core.instr.actions;

import javassist.CtClass;
import javassist.CtMethod;

public interface IActionTaker {

  public static enum Action {
    ACCEPT, NEXT, REJECT
  }

  public Action getAction(final CtClass c);

  public Action getAction(final CtMethod m);
}
