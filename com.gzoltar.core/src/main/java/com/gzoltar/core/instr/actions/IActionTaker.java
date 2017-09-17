package com.gzoltar.core.instr.actions;

import javassist.CtBehavior;
import javassist.CtClass;

public interface IActionTaker {

  public static enum Action {
    ACCEPT, NEXT, REJECT
  }

  public Action getAction(final CtClass c);

  public Action getAction(final CtBehavior b);
}
