package com.gzoltar.core.instr.actions;

import javassist.CtBehavior;
import javassist.CtClass;

public interface IAction {

  public Action getAction(final CtClass c);

  public Action getAction(final CtBehavior b);
}
