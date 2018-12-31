/**
 * Copyright (C) 2018 GZoltar contributors.
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
package com.gzoltar.core.instr.matchers;

import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.filter.MethodNoBodyFilter;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

public class ClassNoLineOfCodeMatcher implements IMatcher {

  private static final MethodNoBodyFilter methodNoBodyFilter = new MethodNoBodyFilter();

  @Override
  public boolean matches(final CtClass ctClass) {
    for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
      if (this.matches(ctBehavior)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    if (methodNoBodyFilter.filter(ctBehavior) == Outcome.REJECT) {
      return false;
    }

    MethodInfo methodInfo = ctBehavior.getMethodInfo();
    CodeAttribute ca = methodInfo.getCodeAttribute();
    if (ca == null) {
      return false;
    }

    CodeIterator ci = ca.iterator();

    try {
      int index = 0;
      while (ci.hasNext()) {
        index = ci.next();
        if (methodInfo.getLineNumber(index) == -1) {
          continue;
        }
        return true;
      }
    } catch (BadBytecode e) {
      throw new RuntimeException(e);
    }

    return false;
  }

  @Override
  public boolean matches(final CtField ctField) {
    throw new RuntimeException("Not implemented");
  }

}
