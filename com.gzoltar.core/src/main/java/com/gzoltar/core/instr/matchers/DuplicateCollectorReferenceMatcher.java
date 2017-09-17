package com.gzoltar.core.instr.matchers;

import com.gzoltar.core.runtime.Collector;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class DuplicateCollectorReferenceMatcher implements IMatcher {

  @Override
  public final boolean matches(final CtClass ctClass) {
    return Collector.instance().existsHitVector(ctClass.getName());
  }

  @Override
  public final boolean matches(final CtBehavior ctBehavior) {
    throw new RuntimeException(DuplicateCollectorReferenceMatcher.class.getSimpleName()
        + " should only be used to filter out duplicate classes from a collector instance");
  }

  @Override
  public final boolean matches(final CtField ctField) {
    throw new RuntimeException(DuplicateCollectorReferenceMatcher.class.getSimpleName()
        + " should only be used to filter out duplicate classes from a collector instance");
  }

}
