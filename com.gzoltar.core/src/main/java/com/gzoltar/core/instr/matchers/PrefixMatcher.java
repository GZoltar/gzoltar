package com.gzoltar.core.instr.matchers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javassist.CtBehavior;
import javassist.CtClass;

public class PrefixMatcher extends AbstractMatcher {

  private List<String> prefix = new LinkedList<String>();

  public PrefixMatcher(final String... strings) {
    this.prefix.addAll(Arrays.asList(strings));
  }

  public PrefixMatcher(final List<String> strings) {
    this.prefix.addAll(strings);
  }

  @Override
  public final boolean matches(final CtClass c) {
    return this.matchesPrefix(c.getName());
  }

  @Override
  public final boolean matches(final CtBehavior b) {
    return this.matchesPrefix(b.getName());
  }

  private boolean matchesPrefix(String name) {
    for (String s : this.prefix) {
      if (name.startsWith(s)) {
        return true;
      }
    }
    return false;
  }

}
