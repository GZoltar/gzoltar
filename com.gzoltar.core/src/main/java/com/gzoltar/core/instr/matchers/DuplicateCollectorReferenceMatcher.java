package com.gzoltar.core.instr.matchers;

import com.gzoltar.core.instr.InstrumentationConstants;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class DuplicateCollectorReferenceMatcher implements IMatcher {

  @Override
  public boolean matches(final CtClass ctClass) {
    for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
      if (this.matches(ctBehavior)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return ctBehavior.getName().equals(InstrumentationConstants.INIT_METHOD_NAME);
  }

  @Override
  public boolean matches(final CtField ctField) {
    return ctField.getName().equals(InstrumentationConstants.FIELD_NAME);
  }

}
