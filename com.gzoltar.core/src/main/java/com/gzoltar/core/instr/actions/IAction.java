package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.Outcome;
import javassist.CtBehavior;
import javassist.CtClass;

public interface IAction {

  public Outcome getAction(final CtClass c);

  public Outcome getAction(final CtBehavior b);
}
