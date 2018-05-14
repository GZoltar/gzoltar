package com.gzoltar.core.instr.pass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.filter.EmptyMethodFilter;
import com.gzoltar.core.runtime.Collector;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;

public abstract class AbstractInitMethodInstrumentationPass implements IPass {

  private static final String METHOD_STR;

  protected static final String ARRAY_OBJECT_NAME = "$tmpGZoltarData";

  private final EmptyMethodFilter emptyMethodFilter = new EmptyMethodFilter();

  private String classHash = null;

  protected String collectorCall = null;

  static {
    METHOD_STR = 
        InstrumentationConstants.INIT_METHOD_DESC_HUMAN + InstrumentationConstants.INIT_METHOD_NAME_WITH_ARGS + " { "
              + "if (" + InstrumentationConstants.FIELD_NAME + " == " + InstrumentationConstants.FIELD_INIT_VALUE + ") { "
                + "Object[] " + ARRAY_OBJECT_NAME + " = new Object[] { \"%s\",\"%s\",\"%d\" }; "
                + "%s "
                + InstrumentationConstants.FIELD_NAME + " = (" + InstrumentationConstants.FIELD_DESC_HUMAN + ") " + ARRAY_OBJECT_NAME + "[0]; "
              + "}"
            + "}";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(CtClass ctClass) throws Exception {
    CtMethod gzoltarInit =
        CtMethod.make(String.format(METHOD_STR, this.classHash, ctClass.getName(),
            Collector.instance().getProbeGroup(ctClass.getName()).getNumberOfProbes(),
            this.collectorCall), ctClass);
    gzoltarInit.setModifiers(gzoltarInit.getModifiers() | InstrumentationConstants.INIT_METHOD_ACC);
    ctClass.addMethod(gzoltarInit);
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

    ctBehavior.insertBefore(
        InstrumentationConstants.INIT_METHOD_NAME_WITH_ARGS + InstrumentationConstants.EOL);
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
