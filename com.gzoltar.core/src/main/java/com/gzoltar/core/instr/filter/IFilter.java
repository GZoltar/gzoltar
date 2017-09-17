package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.Outcome;
import javassist.CtBehavior;
import javassist.CtClass;

public interface IFilter {

  public Outcome filter(final CtClass ctClass);

  public Outcome filter(final CtBehavior ctBehavior);

}
