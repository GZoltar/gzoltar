package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.actions.Action;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.IAction;
import com.gzoltar.core.instr.matchers.AndMatcher;
import com.gzoltar.core.instr.matchers.MethodNameMatcher;
import com.gzoltar.core.instr.matchers.OrMatcher;
import com.gzoltar.core.instr.matchers.SuperclassMatcher;
import com.gzoltar.core.util.VMUtils;
import javassist.CtBehavior;
import javassist.CtClass;

/**
 * Filters methods 'values' and 'valueOf' of enum classes.
 */
public final class EnumFilter extends Filter {

  /**
   * The methods:
   *   public static __ENUM_NAME__ values();
   *   public static __ENUM_NAME__ valueOf(java.lang.String);
   * added by the compiler to an ENUM class *are not* marked as SYNTHETIC. Therefore, we need to
   * explicit ignore them.
   * 
   * see https://bugs.openjdk.java.net/browse/JDK-6520153 for more information.
   * 
   * @param ctClass a class
   */
  @Override
  public Action filter(final CtClass ctClass) {
    IAction enumFilter = this.enumFilterAction(ctClass.getName());
    return super.filter(ctClass, enumFilter);
  }

  @Override
  public Action filter(final CtBehavior ctBehavior) {
    IAction enumFilter = this.enumFilterAction(ctBehavior.getDeclaringClass().getName());
    return super.filter(ctBehavior, enumFilter);
  }

  private IAction enumFilterAction(String className) {
    String classNameWithSlash = VMUtils.toVMName(className);
    IAction enumMethods = new BlackList(new AndMatcher(new SuperclassMatcher("java.lang.Enum"),
        new OrMatcher(new MethodNameMatcher("values" + "()[L" + classNameWithSlash + ";"),
            new MethodNameMatcher(
                "valueOf" + "(Ljava/lang/String;)L" + classNameWithSlash + ";"))));
    return enumMethods;
  }

}
