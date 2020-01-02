/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.IAction;
import com.gzoltar.core.instr.matchers.AndMatcher;
import com.gzoltar.core.instr.matchers.MethodNameMatcher;
import com.gzoltar.core.instr.matchers.OrMatcher;
import com.gzoltar.core.instr.matchers.SuperclassMatcher;
import com.gzoltar.core.util.VMUtils;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.Descriptor;

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
  public Outcome filter(final CtClass ctClass) {
    IAction enumFilter = this.enumFilterAction(ctClass.getName());
    return super.filter(ctClass, enumFilter);
  }

  @Override
  public Outcome filter(final CtBehavior ctBehavior) {
    IAction enumFilter = this.enumFilterAction(ctBehavior.getDeclaringClass().getName());
    return super.filter(ctBehavior, enumFilter);
  }

  private IAction enumFilterAction(final String className) {
    String classNameWithSlash = VMUtils.toVMName(className);
    IAction enumMethods = new BlackList(new AndMatcher(new SuperclassMatcher("java.lang.Enum"),
        new OrMatcher(
            new MethodNameMatcher(
                "values" + Descriptor.toString("()[L" + classNameWithSlash + ";")),
            new MethodNameMatcher("valueOf"
                + Descriptor.toString("(Ljava/lang/String;)L" + classNameWithSlash + ";")))));
    return enumMethods;
  }

}
