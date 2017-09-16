package com.gzoltar.core.instr.matchers;

import java.util.regex.Pattern;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class SuperclassMatcher extends AbstractMatcher {

  private final Pattern pattern;

  public SuperclassMatcher(final String expression) {
    this.pattern = super.matches(expression);
  }

  @Override
  public final boolean matches(final CtClass c) {
    try {
      CtClass superCtClass = c.getSuperclass();
      while (superCtClass != null) {
        if (this.pattern.matcher(superCtClass.getName()).matches()) {
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
  public final boolean matches(final CtMethod m) {
    return false;
  }

}
