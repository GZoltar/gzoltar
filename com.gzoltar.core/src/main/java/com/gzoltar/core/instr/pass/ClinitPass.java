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

import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.util.ClassUtils;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.Modifier;
import javassist.bytecode.analysis.Type;

public class ClinitPass implements IPass {

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(final ClassLoader loader, final CtClass ctClass,
      final String ctClassHash) throws Exception {
    if (ctClass.isInterface() && !ClassUtils.isInterfaceClassSupported(ctClass)) {
      return Outcome.REJECT;
    }

    CtConstructor clinit = ctClass.getClassInitializer();
    if (clinit != null) {
      // clone or adapt <clinit> static constructor to call the .$gzoltarResetter() method
      if (this.transform(loader, ctClass, ctClassHash, clinit) == Outcome.REJECT) {
        return Outcome.REJECT;
      }

      return Outcome.ACCEPT;
    }

    return Outcome.REJECT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(final ClassLoader loader, final CtClass ctClass,
      final String ctClassHash, final CtBehavior clinit) throws Exception {
    if (ctClass.isInterface() && !ClassUtils.isInterfaceClassSupported(ctClass)) {
      return Outcome.REJECT;
    }

    boolean instrumented = false;

    // reset static fields to their default values
    StringBuilder str = new StringBuilder();
    for (CtField ctField : ctClass.getDeclaredFields()) {
      if (!Modifier.isStatic(ctField.getModifiers())) {
        // skip non static fields of being reset
        continue;
      }
      if (ctField.getName().equals("serialVersionUID")) {
        // in theory the field named serialVersionUID is a constant which should not be reset in any
        // circumstances as it will break the serialization of the class. in here, we just make sure
        // the reset does not occur.
        continue;
      }
      if (ctField.getName().startsWith(InstrumentationConstants.PREFIX)) {
        // skip GZoltar fields
        continue;
      }

      Object value = ctField.getConstantValue();
      if (value == null) {
        Type type = Type.get(ctField.getType());

        if (type.isAssignableFrom(Type.DOUBLE)) {
          str.append(ctField.getName() + " = (double) 0.0; ");
        } else if (type.isAssignableFrom(Type.BOOLEAN)) {
          str.append(ctField.getName() + " = (boolean) false; ");
        } else if (type.isAssignableFrom(Type.LONG)) {
          str.append(ctField.getName() + " = (long) 0; ");
        } else if (type.isAssignableFrom(Type.CHAR)) {
          str.append(ctField.getName() + " = (char) '\0'; ");
        } else if (type.isAssignableFrom(Type.BYTE)) {
          str.append(ctField.getName() + " = (byte) 0; ");
        } else if (type.isAssignableFrom(Type.SHORT)) {
          str.append(ctField.getName() + " = (short) 0; ");
        } else if (type.isAssignableFrom(Type.INTEGER)) {
          str.append(ctField.getName() + " = (int) 0; ");
        } else if (type.isAssignableFrom(Type.FLOAT)) {
          str.append(ctField.getName() + " = (float) 0.0; ");
        } else { // Object || Array
          str.append(ctField.getName() + " = null; ");
        }

        instrumented = true;
      } else {
        // non-null fields are handled by the <clinit> method itself
        // str.append(ctField.getName() + " = " + value + "; ");
      }
    }

    if (!instrumented) {
      return Outcome.REJECT;
    }

    CtConstructor staticConstructorClone = new CtConstructor((CtConstructor) clinit, ctClass, null);
    staticConstructorClone.getMethodInfo()
        .setName(InstrumentationConstants.CLINIT_CLONE_METHOD_NAME);
    staticConstructorClone
        .setModifiers(ctClass.isInterface() ? InstrumentationConstants.CLINIT_CLONE_METHOD_INTF_ACC
            : InstrumentationConstants.CLINIT_CLONE_METHOD_ACC);
    staticConstructorClone.insertBefore(str.toString());
    ctClass.addConstructor(staticConstructorClone);

    // replace body of original <clinit> method with a call to clinit clone
    clinit.setBody("{ " + InstrumentationConstants.CLINIT_CLONE_METHOD_NAME_WITH_ARGS + "; }");

    return Outcome.ACCEPT;
  }
}
