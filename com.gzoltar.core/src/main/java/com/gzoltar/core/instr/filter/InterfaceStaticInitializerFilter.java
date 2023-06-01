package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.Outcome;
import javassist.CtBehavior;
import javassist.bytecode.MethodInfo;

public class InterfaceStaticInitializerFilter extends Filter {

  @Override
  public Outcome filter(final CtBehavior ctBehavior) {
    MethodInfo methodInfo = ctBehavior.getMethodInfo();
    if ((ctBehavior.getDeclaringClass() != null && ctBehavior.getDeclaringClass().isInterface())
        && methodInfo.isStaticInitializer()
        && !methodInfo.isMethod()) {
      return Outcome.REJECT;
    }
    return Outcome.ACCEPT;
  }
}
