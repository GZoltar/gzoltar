package com.gzoltar.core.instr.matchers;

import com.gzoltar.core.runtime.Collector;
import javassist.CtClass;
import javassist.CtMethod;

public class DuplicateCollectorReferenceMatcher extends AbstractMatcher {

  @Override
  public final boolean matches(final CtClass c) {
    return Collector.instance().existsHitVector(c.getName());
  }

  @Override
  public final boolean matches(final CtMethod m) {
    return false;
  }
}
