package com.gzoltar.core.instr.granularity;

import javassist.CtClass;
import javassist.bytecode.MethodInfo;

public abstract class AbstractGranularity implements IGranularity {

  protected CtClass ctClass;

  protected MethodInfo methodInfo;

  /**
   * 
   * @param ctClass
   * @param methodInfo
   */
  public AbstractGranularity(final CtClass ctClass, final MethodInfo methodInfo) {
    this.ctClass = ctClass;
    this.methodInfo = methodInfo;
  }

}
