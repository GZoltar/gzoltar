package com.gzoltar.core.instr.granularity;

import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

public class LineGranularity extends AbstractGranularity {

  private int currentLine = -1;

  public LineGranularity(final CtClass ctClass, final MethodInfo methodInfo,
      final CodeIterator codeIterator) {
    super(ctClass, methodInfo, codeIterator);
  }

  @Override
  public boolean instrumentAtIndex(final int index, final int instrumentationSize) {
    int previousLine = this.currentLine;
    this.currentLine = this.methodInfo.getLineNumber(index);
    return this.currentLine != previousLine;
  }

  @Override
  public boolean stopInstrumenting() {
    return false;
  }

}
