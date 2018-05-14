package com.gzoltar.core.instr.pass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.filter.EmptyMethodFilter;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;

public class InitMethodInstrumentationPass implements IPass {

  private String hash = null;

  private static final String methodStr;

  private final EmptyMethodFilter emptyMethodFilter;

  public InitMethodInstrumentationPass() {
    this.emptyMethodFilter = new EmptyMethodFilter();
  }

  static {
    methodStr =
        InstrumentationConstants.INIT_METHOD_DESC_HUMAN + InstrumentationConstants.INIT_METHOD_NAME_WITH_ARGS + " { "
              + "if (" + InstrumentationConstants.FIELD_NAME + " == " + InstrumentationConstants.FIELD_INIT_VALUE + ") { "
                + "Object[] $tmpGZoltarData = new Object[] { \"%s\",\"%s\" }; "
                + InstrumentationConstants.SYSTEM_CLASS_NAME_JVM + "." + InstrumentationConstants.SYSTEM_CLASS_FIELD_NAME + ".equals($tmpGZoltarData); "
                + InstrumentationConstants.FIELD_NAME + " = (" + InstrumentationConstants.FIELD_DESC_HUMAN + ") $tmpGZoltarData[0]; "
              + "}"
            + "}";
  }

  @Override
  public Outcome transform(CtClass ctClass) throws Exception {
    CtMethod gzoltarInit =
        CtMethod.make(String.format(methodStr, this.hash, ctClass.getName()), ctClass);
    gzoltarInit.setModifiers(gzoltarInit.getModifiers() | InstrumentationConstants.INIT_METHOD_ACC);
    ctClass.addMethod(gzoltarInit);
    return Outcome.ACCEPT;
  }

  @Override
  public Outcome transform(CtClass ctClass, CtBehavior ctBehavior) throws Exception {
    if (this.emptyMethodFilter.filter(ctBehavior) == Outcome.REJECT) {
      return Outcome.REJECT;
    }

    ctBehavior.insertBefore(
        InstrumentationConstants.INIT_METHOD_NAME_WITH_ARGS + InstrumentationConstants.EOL);
    return Outcome.ACCEPT;
  }

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

    this.hash = sb.toString();
  }

}
