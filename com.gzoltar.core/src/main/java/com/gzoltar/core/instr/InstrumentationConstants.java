package com.gzoltar.core.instr;

import com.gzoltar.core.runtime.Collector;
import javassist.bytecode.AccessFlag;

/**
 * Constants for byte code instrumentation.
 */
public final class InstrumentationConstants {

  public static final String EOL = ";";

  // === System Class' Field ===

  public static final String SYSTEM_CLASS_NAME = "java.lang.UnknownError";

  public static final String SYSTEM_CLASS_NAME_JVM = "java/lang/UnknownError";

  public static final String SYSTEM_CLASS_FIELD_NAME = "$gzoltarAccess";

  public static final String SYSTEM_CLASS_FIELD_DESC = "Object ";

  public static final String SYSTEM_CLASS_FIELD_JVM_DESC = "java/lang/Object";

  public static final String SYSTEM_CLASS_FIELD_TYPE = "Ljava/lang/Object;";

  public static final int SYSTEM_CLASS_FIELD_ACC =
      AccessFlag.PUBLIC | AccessFlag.STATIC | AccessFlag.SYNTHETIC | AccessFlag.TRANSIENT;

  public static final String SYSTEM_CLASS_METHOD_NAME = "equals";

  public static final String SYSTEM_CLASS_METHOD_SIGNATURE = "(" + SYSTEM_CLASS_FIELD_TYPE + ")Z";

  // === Data Field ===

  public static final String FIELD_NAME = "$gzoltarData";

  public static final String FIELD_DESC_BYTECODE = "[Z";

  public static final int FIELD_ACC =
      AccessFlag.PRIVATE | AccessFlag.STATIC | AccessFlag.SYNTHETIC | AccessFlag.TRANSIENT;

  public static final int FIELD_INTF_ACC =
      AccessFlag.PRIVATE | AccessFlag.STATIC | AccessFlag.FINAL | AccessFlag.SYNTHETIC;

  // === Init method ===

  public static final String INIT_METHOD_NAME = "$gzoltarInit";

  public static final String INIT_METHOD_SIGNATURE = "()V";

  public static final int INIT_METHOD_ACC =
      AccessFlag.PRIVATE | AccessFlag.STATIC | AccessFlag.SYNTHETIC;

  // TODO we may need extra constants for the init method of Java-8 interfaces

  // === Collector ===

  public static final String COLLECTOR_NAME = Collector.class.getCanonicalName();

  public static final String COLLECTOR_JVM_NAME = COLLECTOR_NAME.replace(".", "/");

  public static final String COLLECTOR_INSTANCE = "instance";

  public static final String COLLECTOR_INSTANCE_SIGNATURE = "()L" + COLLECTOR_JVM_NAME + ";";

  public static final String COLLECTOR_METHOD_NAME = "getHitArray";

  public static final String COLLECTOR_METHOD_SIGNATURE = "([Ljava/lang/Object;)V";

  private InstrumentationConstants() {
    // NO-OP
  }
}
