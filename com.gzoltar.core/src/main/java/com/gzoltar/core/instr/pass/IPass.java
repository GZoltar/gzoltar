package com.gzoltar.core.instr.pass;

import javassist.CtClass;

public interface IPass {

  public static enum Outcome {
    CONTINUE, CANCEL, FINISH
  };

  public Outcome transform(final CtClass c) throws Exception;
}
