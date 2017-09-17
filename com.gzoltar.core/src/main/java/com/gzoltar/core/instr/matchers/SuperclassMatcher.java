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
  public final boolean matches(final CtClass ctClass) {
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
  public final boolean matches(final CtBehavior ctBehavior) {
    throw new RuntimeException(SuperclassMatcher.class.getSimpleName()
        + " should only be used to filter out super classes");
  }

  @Override
  public final boolean matches(final CtField ctField) {
    throw new RuntimeException(SuperclassMatcher.class.getSimpleName()
        + " should only be used to filter out super classes");
  }

}
