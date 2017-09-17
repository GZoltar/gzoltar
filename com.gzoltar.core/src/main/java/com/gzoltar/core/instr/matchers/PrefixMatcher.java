package com.gzoltar.core.instr.matchers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class PrefixMatcher implements IMatcher {

  private List<String> prefix = new LinkedList<String>();

  public PrefixMatcher(final String... strings) {
    this.prefix.addAll(Arrays.asList(strings));
  }

  public PrefixMatcher(final List<String> strings) {
    this.prefix.addAll(strings);
  }

  @Override
  public final boolean matches(final CtClass ctClass) {
    return this.matchesPrefix(ctClass.getName());
  }

  @Override
  public final boolean matches(final CtBehavior ctBehavior) {
    return this.matchesPrefix(ctBehavior.getName());
  }

  @Override
  public final boolean matches(final CtField ctField) {
    return this.matchesPrefix(ctField.getName());
  }

  private boolean matchesPrefix(String name) {
    for (String p : this.prefix) {
      if (name.startsWith(p)) {
        return true;
      }
    }
    return false;
  }

}
