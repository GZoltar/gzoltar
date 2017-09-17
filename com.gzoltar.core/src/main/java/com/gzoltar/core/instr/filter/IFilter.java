package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.actions.Action;
import javassist.CtBehavior;
import javassist.CtClass;

public interface IFilter {

  public Action filter(final CtClass ctClass);

  public Action filter(final CtBehavior ctBehavior);

}
