package com.gzoltar.core.instr.pass;

import javassist.CtBehavior;
import javassist.CtClass;

public interface IPass {

  public static enum Outcome {
    CONTINUE, CANCEL, FINISH
  };

  public Outcome transform(final CtClass c) throws Exception;

  public Outcome transform(final CtClass c, final CtBehavior m) throws Exception;
}
