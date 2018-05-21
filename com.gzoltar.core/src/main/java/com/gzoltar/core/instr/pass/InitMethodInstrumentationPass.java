package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.InstrumentationConstants;
import javassist.bytecode.Bytecode;
import javassist.bytecode.Opcode;

public class InitMethodInstrumentationPass extends AbstractInitMethodInstrumentationPass {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void insertCollectorCall(Bytecode code) {
    // call 'equals' method in the system class
    code.addOpcode(Opcode.ASTORE_1);
    code.addGetstatic(InstrumentationConstants.SYSTEM_CLASS_NAME_JVM,
        InstrumentationConstants.SYSTEM_CLASS_FIELD_NAME,
        InstrumentationConstants.SYSTEM_CLASS_FIELD_TYPE);
    code.addOpcode(Opcode.ALOAD_1);
    code.addInvokevirtual(InstrumentationConstants.SYSTEM_CLASS_FIELD_JVM_DESC,
        InstrumentationConstants.SYSTEM_CLASS_METHOD_NAME,
        InstrumentationConstants.SYSTEM_CLASS_METHOD_SIGNATURE);
    code.addOpcode(Opcode.POP);
  }

}
