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
package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.filter.EmptyMethodFilter;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public class PutGetStaticPass implements IPass {

  private final EmptyMethodFilter emptyMethodFilter = new EmptyMethodFilter();

  private final InstrumentationLevel instrumentationLevel;

  public PutGetStaticPass(final InstrumentationLevel instrumentationLevel) {
    this.instrumentationLevel = instrumentationLevel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(final ClassLoader loader, final CtClass ctClass,
      final String ctClassHash) throws Exception {
    if (this.instrumentationLevel == InstrumentationLevel.NONE) {
      return Outcome.REJECT;
    }

    // Add an empty $gzoltarReseter method to avoid *any* java.lang.NoSuchMethodError when a static
    // field is accessed (either write or read access) by another class
    ResetPass.makeEmptyResetter(ctClass);

    for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
      this.transform(loader, ctClass, ctBehavior);
    }

    return Outcome.ACCEPT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(final ClassLoader loader, final CtClass ctClass,
      final CtBehavior ctBehavior) throws Exception {
    if (this.emptyMethodFilter.filter(ctBehavior) == Outcome.REJECT) {
      // skip empty methods
      return Outcome.ACCEPT;
    }

    MethodInfo methodInfo = ctBehavior.getMethodInfo();
    CodeAttribute ca = methodInfo.getCodeAttribute();

    assert ca != null;
    CodeIterator ci = ca.iterator();

    int index = 0;
    while (ci.hasNext()) {
      index = ci.next();

      int curLine = methodInfo.getLineNumber(index);
      if (curLine == -1) {
        continue;
      }

      //String fieldName = null;
      String className = null;

      int op = ci.byteAt(index);
      if (op == Opcode.PUTSTATIC || op == Opcode.GETSTATIC) {
        int targetFieldAddr = (ci.byteAt(index + 1) << 8) + ci.byteAt(index + 2);
        //fieldName = methodInfo.getConstPool().getFieldrefName(targetFieldAddr);
        className = methodInfo.getConstPool().getFieldrefClassName(targetFieldAddr);

        if (className.equals(ctClass.getName())) {
          // skip calls to its own static fields
          continue;
        }
        if (className.startsWith("javax.") || className.startsWith("java.")
            || className.startsWith("sun.") || className.startsWith("com.sun.")) {
          // skip calls to java classes, "javax.", "java.", "sun.", "com.sun."
          // TODO are there any others we might need to exclude?
          continue;
        }
      } else {
        continue;
      }

      /**
       * if instruction is a static call to a static field in another class, add an instruction to
       * call $_clinit_clone_ of the target class before the call to the static field.
       */
      Bytecode b = new Bytecode(methodInfo.getConstPool());
      b.addInvokestatic(className, "$gzoltarResetter", "()V"); // FIXME harcoded string
      ci.insert(index, b.get());
    }

    return Outcome.ACCEPT;
  }

}