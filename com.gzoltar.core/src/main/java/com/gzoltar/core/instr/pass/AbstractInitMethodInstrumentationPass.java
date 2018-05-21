package com.gzoltar.core.instr.pass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.filter.EmptyMethodFilter;
import com.gzoltar.core.runtime.Collector;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public abstract class AbstractInitMethodInstrumentationPass implements IPass {

  private final EmptyMethodFilter emptyMethodFilter = new EmptyMethodFilter();

  private String classHash = null;

  private Bytecode insertArray(CtClass ctClass, Bytecode code) {
    // get boolean array
    code.addGetstatic(ctClass, InstrumentationConstants.FIELD_NAME,
        InstrumentationConstants.FIELD_DESC_BYTECODE);

    // check whether the boolean array is null
    code.addOpcode(Opcode.ACONST_NULL);
    code.addOpcode(Opcode.IF_ACMPEQ);
    code.addIndex(4); // jump to the 'return' instruction
    code.addReturn(null);

    // if boolean array is null, create an array with 3 Objects
    code.addOpcode(Opcode.ICONST_3);
    code.addAnewarray(InstrumentationConstants.SYSTEM_CLASS_FIELD_JVM_DESC);

    // Class hash
    code.addOpcode(Opcode.DUP);
    code.addOpcode(Opcode.ICONST_0);
    code.addLdc(this.classHash);
    code.addOpcode(Opcode.AASTORE);

    // Class name
    code.addOpcode(Opcode.DUP);
    code.addOpcode(Opcode.ICONST_1);
    code.addLdc(ctClass.getName());
    code.addOpcode(Opcode.AASTORE);

    // Number of probes
    code.addOpcode(Opcode.DUP);
    code.addOpcode(Opcode.ICONST_2);
    code.addLdc(
        String.valueOf(Collector.instance().getProbeGroup(ctClass.getName()).getNumberOfProbes()));
    code.addOpcode(Opcode.AASTORE);

    return code;
  }

  /**
   * 
   * @param code
   */
  protected abstract void insertCollectorCall(Bytecode code);

  private void castArray(CtClass ctClass, Bytecode code) {
    // cast the [0] object of the array (which has been modified by the runtime collector)
    code.addOpcode(Opcode.ALOAD_1);
    code.addOpcode(Opcode.ICONST_0);
    code.addOpcode(Opcode.AALOAD);
    code.addCheckcast(InstrumentationConstants.FIELD_DESC_BYTECODE);
    code.addPutstatic(ctClass, InstrumentationConstants.FIELD_NAME,
        InstrumentationConstants.FIELD_DESC_BYTECODE);
  }

  private void bye(Bytecode code) {
    // bye
    code.addReturn(null);
  }

  private void addInitMethod(CtClass ctClass, Bytecode code) throws Exception {
    ClassFile classFile = ctClass.getClassFile();

    // create a new method and set its access flags
    MethodInfo info =
        new MethodInfo(classFile.getConstPool(), InstrumentationConstants.INIT_METHOD_NAME, "()V");
    info.setCodeAttribute(code.toCodeAttribute());
    info.setAccessFlags(info.getAccessFlags() | InstrumentationConstants.INIT_METHOD_ACC);

    CodeAttribute ca = info.getCodeAttribute();
    int maxStack = ca.computeMaxStack();
    ca.setMaxStack(maxStack);
    ca.setMaxLocals(2);

    // add method's bytecode to the class
    classFile.addMethod(info);

    // rebuild stack map (if required)
    info.rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(CtClass ctClass) throws Exception {
    ClassFile classFile = ctClass.getClassFile();
    Bytecode code = new Bytecode(classFile.getConstPool());

    // create an array with the required objects
    this.insertArray(ctClass, code);
    // add bytecode to call the runtime collector
    this.insertCollectorCall(code);
    // cast the first element of the array to a boolean array
    this.castArray(ctClass, code);
    // last bytecode instruction
    this.bye(code);
    // add a new method to the class
    this.addInitMethod(ctClass, code);

    return Outcome.ACCEPT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(CtClass ctClass, CtBehavior ctBehavior) throws Exception {
    if (this.emptyMethodFilter.filter(ctBehavior) == Outcome.REJECT) {
      return Outcome.REJECT;
    }

    MethodInfo info = ctBehavior.getMethodInfo();
    CodeAttribute codeAttribute = info.getCodeAttribute();

    Bytecode byteCode = new Bytecode(codeAttribute.getConstPool());
    byteCode.addInvokestatic(ctClass, InstrumentationConstants.INIT_METHOD_NAME,
        InstrumentationConstants.INIT_METHOD_SIGNATURE);
    CodeAttribute cA = byteCode.toCodeAttribute();

    CodeIterator it = codeAttribute.iterator();
    it.insert(0, cA.getCode());

    return Outcome.ACCEPT;
  }

  /**
   * 
   * @param bytes
   * @throws NoSuchAlgorithmException
   */
  public void setHash(byte[] bytes) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(bytes);

    // get the hash's bytes
    byte[] hashBytes = md.digest();
    // convert bytes to hexadecimal format
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < hashBytes.length; i++) {
      sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    this.classHash = sb.toString();
  }

}
