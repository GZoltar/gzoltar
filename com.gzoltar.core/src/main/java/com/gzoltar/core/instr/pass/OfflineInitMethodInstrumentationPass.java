package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.InstrumentationConstants;
import javassist.bytecode.Bytecode;
import javassist.bytecode.Opcode;

public class OfflineInitMethodInstrumentationPass extends AbstractInitMethodInstrumentationPass {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void insertCollectorCall(Bytecode code) {
    // insert bytecode to call GZoltar's runtime Collector
    code.addOpcode(Opcode.ASTORE_1);
    code.addInvokestatic(InstrumentationConstants.COLLECTOR_JVM_NAME,
        InstrumentationConstants.COLLECTOR_INSTANCE,
        InstrumentationConstants.COLLECTOR_INSTANCE_SIGNATURE);
    code.addOpcode(Opcode.ALOAD_1);
    code.addInvokevirtual(InstrumentationConstants.COLLECTOR_JVM_NAME,
        InstrumentationConstants.COLLECTOR_METHOD_NAME,
        InstrumentationConstants.COLLECTOR_METHOD_SIGNATURE);
  }

}
