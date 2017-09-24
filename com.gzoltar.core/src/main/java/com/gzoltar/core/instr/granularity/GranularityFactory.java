package com.gzoltar.core.instr.granularity;

import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

public class GranularityFactory {

  public static IGranularity getGranularity(final CtClass ctClass, final MethodInfo methodInfo,
      final CodeIterator codeIterator, final GranularityLevel level) {
    switch (level) {
      case LINE:
        return new LineGranularity(ctClass, methodInfo, codeIterator);
      case METHOD:
        return new MethodGranularity(ctClass, methodInfo, codeIterator);
      case BASICBLOCK:
      default:
        return new BasicBlockGranularity(ctClass, methodInfo, codeIterator);
    }
  }

}
