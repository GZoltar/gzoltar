package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.Outcome;
import javassist.CtBehavior;
import javassist.CtClass;

public interface IPass {

  public Outcome transform(final CtClass ctClass) throws Exception;

  public Outcome transform(final CtClass ctClass, final CtBehavior ctBehavior) throws Exception;

}
