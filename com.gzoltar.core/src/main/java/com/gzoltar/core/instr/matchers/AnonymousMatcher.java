package com.gzoltar.core.instr.matchers;

import com.gzoltar.core.util.ClassUtils;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class AnonymousMatcher implements IMatcher {

  @Override
  public boolean matches(final CtClass ctClass) {
    return ClassUtils.isAnonymousClass(ctClass);
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return this.matches(ctBehavior.getDeclaringClass());
  }

  @Override
  public boolean matches(final CtField ctField) {
    return this.matches(ctField.getDeclaringClass());
  }
}
