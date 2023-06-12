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
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.ClassFile;

/**
 * Filters Java-7 interfaces from being instrumented by GZoltar as those classes do
 * not allow static methods under Java-7.
 */
public final class Java7InterfaceFilter extends Filter {

  @Override
  public Outcome filter(final CtClass ctClass) {
    return this.filterAction(ctClass);
  }

  @Override
  public Outcome filter(final CtBehavior ctBehavior) {
    return this.filterAction(ctBehavior.getDeclaringClass());
  }

  private Outcome filterAction(final CtClass ctClass) {
    if (ctClass.isInterface()) {
      int majorVersion = ctClass.getClassFile2().getMajorVersion();
      if (majorVersion <= ClassFile.JAVA_7) {
        return Outcome.REJECT;
      }
    }
    return Outcome.ACCEPT;
  }

}
