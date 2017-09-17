package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.Outcome;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.MethodInfo;

public class StackSizePass implements IPass {

  @Override
  public final Outcome transform(final CtClass ctClass) throws Exception {
    for (CtBehavior b : ctClass.getDeclaredBehaviors()) {
      this.transform(ctClass, b);
    }

    return Outcome.NEXT;
  }

  @Override
  public final Outcome transform(final CtClass ctClass, final CtBehavior ctBehavior) throws Exception {
    MethodInfo info = ctBehavior.getMethodInfo();
    CodeAttribute ca = info.getCodeAttribute();

    if (ca != null) {
      int ss = ca.computeMaxStack();
      ca.setMaxStack(ss);
      // info.rebuildStackMapIf6(cp, c.getClassFile()); // TODO how to get classpool?!
    }

    return Outcome.NEXT;
  }
}
