package com.gzoltar.core.util;

import javassist.CtClass;

public class ClassUtils {

  /**
   * 
   * @param clazz
   * @return
   */
  public static boolean isAnonymousClass(CtClass clazz) {
    // return cc.getClassFile2().getInnerAccessFlags() == 8;
    int pos = clazz.getName().lastIndexOf('$');
    if (pos < 0) {
      return false;
    }
    return Character.isDigit(clazz.getName().charAt(pos + 1));
  }
}
