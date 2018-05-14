package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.InstrumentationConstants;

public class InitMethodInstrumentationPass extends AbstractInitMethodInstrumentationPass {

  private static final String call = InstrumentationConstants.SYSTEM_CLASS_NAME_JVM + "."
      + InstrumentationConstants.SYSTEM_CLASS_FIELD_NAME + ".equals(" + ARRAY_OBJECT_NAME + "); ";

  public InitMethodInstrumentationPass() {
    this.collectorCall = call;
  }

}
