package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.matchers.IMatcher;
import javassist.CtClass;
import javassist.CtMethod;

public abstract class AbstractActionTaker implements IActionTaker {

  private final IMatcher matcher;

  public AbstractActionTaker(IMatcher matcher) {
    this.matcher = matcher;
  }

  @Override
  public final Action getAction(CtClass c) {
    return getAction(this.matcher.matches(c));
  }

  @Override
  public final Action getAction(CtMethod m) {
    return getAction(this.matcher.matches(m));
  }

  protected abstract Action getAction(boolean matches);
}
