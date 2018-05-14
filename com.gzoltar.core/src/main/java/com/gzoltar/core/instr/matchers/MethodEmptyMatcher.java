package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.MethodInfo;

public class MethodEmptyMatcher implements IMatcher {

  @Override
  public boolean matches(CtClass ctClass) {
    return false;
  }

  @Override
  public boolean matches(CtBehavior ctBehavior) {
    MethodInfo methodInfo = ctBehavior.getMethodInfo();
    if (!methodInfo.isConstructor() && !methodInfo.isStaticInitializer() && ctBehavior.isEmpty()) {
      return true;
    }
    return false;
  }

  @Override
  public boolean matches(CtField ctField) {
    return false;
  }

}
