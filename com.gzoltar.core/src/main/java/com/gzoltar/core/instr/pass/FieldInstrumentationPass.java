package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.Outcome;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;

public class FieldInstrumentationPass implements IPass {

  @Override
  public Outcome transform(CtClass ctClass) throws Exception {
    ClassFile classFile = ctClass.getClassFile();

    // create a new field and set its access flags
    FieldInfo field = new FieldInfo(classFile.getConstPool(), InstrumentationConstants.FIELD_NAME,
        InstrumentationConstants.FIELD_DESC_BYTECODE);
    field.setAccessFlags(field.getAccessFlags() | InstrumentationConstants.FIELD_ACC);

    // add new field to the class
    classFile.addField(field);

    return Outcome.ACCEPT;
  }

  @Override
  public Outcome transform(CtClass ctClass, CtBehavior ctBehavior) throws Exception {
    return Outcome.REJECT;
  }

}
