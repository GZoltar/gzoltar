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
package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.Outcome;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.MethodInfo;

public class StackSizePass implements IPass {

  @Override
  public Outcome transform(final CtClass ctClass) throws Exception {
    for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
      if (this.transform(ctClass, ctBehavior) == Outcome.REJECT) {
        return Outcome.REJECT;
      }
    }
    return Outcome.ACCEPT;
  }

  @Override
  public Outcome transform(final CtClass ctClass, final CtBehavior ctBehavior) throws Exception {
    MethodInfo info = ctBehavior.getMethodInfo();
    CodeAttribute ca = info.getCodeAttribute();

    if (ca != null) {
      int ss = ca.computeMaxStack();
      ca.setMaxStack(ss);
      info.rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile());
    }

    return Outcome.ACCEPT;
  }

}
