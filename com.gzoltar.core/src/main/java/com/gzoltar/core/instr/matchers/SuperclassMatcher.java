package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

public class SuperclassMatcher extends AbstractWildcardMatcher {

  public SuperclassMatcher(final String expression) {
    super(expression);
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    try {
      CtClass superCtClass = ctClass.getSuperclass();
      while (superCtClass != null) {
        if (super.matches(superCtClass.getName())) {
          return true;
        }
        superCtClass = superCtClass.getSuperclass();
      }
      return false;
    } catch (NotFoundException e) {
      return false;
    }
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
