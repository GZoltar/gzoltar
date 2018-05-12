package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.Outcome;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class FieldInstrumentationPass implements IPass {

  private static final String fieldStr =
      InstrumentationConstants.FIELD_DESC_HUMAN + InstrumentationConstants.FIELD_NAME + " = "
          + InstrumentationConstants.FIELD_INIT_VALUE + InstrumentationConstants.EOL;

  @Override
  public Outcome transform(CtClass ctClass) throws Exception {
    CtField f = CtField.make(fieldStr, ctClass);
    f.setModifiers(f.getModifiers() | InstrumentationConstants.FIELD_ACC);
    ctClass.addField(f);

    return Outcome.ACCEPT;
  }

  @Override
  public Outcome transform(CtClass ctClass, CtBehavior ctBehavior) throws Exception {
    return Outcome.REJECT;
  }

}
