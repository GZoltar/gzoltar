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
package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.util.ClassUtils;
import javassist.CtBehavior;
import javassist.bytecode.MethodInfo;

/**
 * Filters the constructor of an Anonymous class.
 */
public final class AnonymousClassConstructorFilter extends Filter {

  @Override
  public Outcome filter(final CtBehavior ctBehavior) {
    MethodInfo methodInfo = ctBehavior.getMethodInfo();
    if (ClassUtils.isAnonymousClass(ctBehavior.getDeclaringClass()) && methodInfo.isConstructor()
        && !methodInfo.isMethod()) {
      return Outcome.REJECT;
    }
    return Outcome.ACCEPT;
  }
}
