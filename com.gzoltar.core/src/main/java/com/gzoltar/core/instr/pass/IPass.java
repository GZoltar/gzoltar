package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.actions.Action;
import javassist.CtBehavior;
import javassist.CtClass;

public interface IPass {

  public Action transform(final CtClass ctClass) throws Exception;

  public Action transform(final CtClass ctClass, final CtBehavior ctBehavior) throws Exception;
}
