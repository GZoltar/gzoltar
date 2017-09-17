package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.matchers.AndMatcher;
import com.gzoltar.core.instr.matchers.MethodNameMatcher;
import com.gzoltar.core.instr.matchers.OrMatcher;
import com.gzoltar.core.instr.matchers.SuperclassMatcher;

/**
 * Filters methods 'values' and 'valueOf' of enum classes.
 */
public final class EnumPass extends FilterPass {

  /**
   * The methods:
   *   public static __ENUM_NAME__ values();
   *   public static __ENUM_NAME__ valueOf(java.lang.String);
   * added by the compiler to an ENUM class *are not* marked as SYNTHETIC. Therefore, we need to
   * explicit ignore them.
   * 
   * see https://bugs.openjdk.java.net/browse/JDK-6520153 for more information.
   * 
   * @param className name of class using VM notation, i.e., '/' instead of '.'
   */
  public EnumPass(String className) {
    BlackList enumMethods = new BlackList(new AndMatcher(new SuperclassMatcher("java.lang.Enum"),
        new OrMatcher(new MethodNameMatcher("values" + "()[L" + className + ";"),
            new MethodNameMatcher("valueOf" + "(Ljava/lang/String;)L" + className + ";"))));
    this.add(enumMethods);
  }

}
