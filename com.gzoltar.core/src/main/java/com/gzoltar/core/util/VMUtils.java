package com.gzoltar.core.util;

public final class VMUtils {

  public final static String toVMName(final String className) {
    return className.replace('.', '/');
  }

}
