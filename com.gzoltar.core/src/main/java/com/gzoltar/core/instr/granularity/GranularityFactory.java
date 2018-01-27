package com.gzoltar.core.instr.granularity;

import javassist.CtClass;
import javassist.bytecode.MethodInfo;

public class GranularityFactory {

  public static IGranularity getGranularity(final CtClass ctClass, final MethodInfo methodInfo,
      final GranularityLevel level) {
    switch (level) {
      case LINE:
        return new LineGranularity(ctClass, methodInfo);
      case METHOD:
        return new MethodGranularity(ctClass, methodInfo);
      case BASICBLOCK:
      default:
        return new BasicBlockGranularity(ctClass, methodInfo);
    }
  }

}
