package com.gzoltar.core.instr.pass;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.MethodInfo;

public class StackSizePass implements IPass {

  @Override
  public final Outcome transform(final CtClass c) throws Exception {
    for (CtBehavior b : c.getDeclaredBehaviors()) {
      MethodInfo info = b.getMethodInfo();
      CodeAttribute ca = info.getCodeAttribute();

      if (ca != null) {
        int ss = ca.computeMaxStack();
        ca.setMaxStack(ss);
        // info.rebuildStackMapIf6(cp, c.getClassFile()); // TODO how to get classpool?!
      }
    }

    return Outcome.CONTINUE;
  }
}
