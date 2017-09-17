package com.gzoltar.core.instr.matchers;

import java.util.regex.Pattern;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class FieldNameMatcher extends AbstractMatcher {

  private final Pattern pattern;

  public FieldNameMatcher(final String expression) {
    this.pattern = super.matches(expression);
  }

  @Override
  public final boolean matches(final CtClass c) {
    for (CtField field : c.getDeclaredFields()) {
      if (this.pattern.matcher(field.getName()).matches()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public final boolean matches(final CtBehavior b) {
    return false;
  }
}
