package com.gzoltar.core.instr;

public enum InstrumentationLevel {

  /**
   * A FULL instrumentation level instruments a class (i.e., adds probes to a particular class) and
   * generates all nodes (i.e., information about probes).
   */
  FULL,

  /**
   * A NONE instrumentation level only generates nodes (i.e., information about probes). No
   * bytecode is ever injected.
   */
  NONE,

  /**
   * A OFFLINE instrumentation level only instruments a class (i.e., adds probes to a particular
   * class).
   */
  OFFLINE

}
