package com.gzoltar.core.instr.granularity;

import com.gzoltar.core.model.Node;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.MethodInfo;

public class MethodGranularity extends AbstractGranularity {

  public MethodGranularity(final CtClass ctClass, final MethodInfo methodInfo) {
    super(ctClass, methodInfo);
  }

  @Override
  public boolean instrumentAtIndex(final int index, final int instrumentationSize) {
    return true;
  }

  @Override
  public boolean stopInstrumenting() {
    return true;
  }

  @Override
  public Node getNode(final CtClass ctClass, final CtBehavior ctBehavior, final int lineNumber) {
    return super.getNode(ctBehavior, lineNumber);
  }

}
