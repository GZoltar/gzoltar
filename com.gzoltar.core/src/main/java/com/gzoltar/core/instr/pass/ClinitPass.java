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

import com.gzoltar.core.instr.Outcome;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.Modifier;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.analysis.Type;

public class ClinitPass implements IPass {

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(final ClassLoader loader, final CtClass ctClass,
      final String ctClassHash) throws Exception {
    if (ctClass.isInterface()) {
      // TODO add support for interfaces
      return Outcome.REJECT;
    }

    CtConstructor clinit = ctClass.makeClassInitializer();
    if (clinit != null) {
      // clone <clinit> method
      if (this.transform(loader, ctClass, clinit) == Outcome.REJECT) {
        return Outcome.REJECT;
      }

      // replace body of original <clinit> method with a call to clinit clone
      clinit.setBody("{ $_clinit_clone_(); }"); // FIXME hardcoded string
      return Outcome.ACCEPT;
    }

    return Outcome.REJECT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(final ClassLoader loader, final CtClass ctClass, final CtBehavior clinit)
      throws Exception {
    CtConstructor staticConstructorClone = new CtConstructor((CtConstructor) clinit, ctClass, null);
    staticConstructorClone.getMethodInfo().setName("$_clinit_clone_"); // FIXME hardcoded string
    staticConstructorClone.setModifiers(AccessFlag.PRIVATE | AccessFlag.STATIC); // FIXME hardcoded access

    // reset static fields to their default values
    StringBuilder str = new StringBuilder();
    for (CtField ctField : ctClass.getDeclaredFields()) {
      if (!Modifier.isStatic(ctField.getModifiers())) {
        // skip static final fields of being reset
        continue;
      }
      if (ctField.getName().equals("serialVersionUID")) {
        // in theory the field named serialVersionUID is a constant which should not be reset in any
        // circumstances as it will break the serialization of the class. in here, we just make sure
        // the reset does not occur.
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
      } else {
        // non-null fields are handled by the <clinit> method itself (which at this point has been
        // cloned)
        // str.append(ctField.getName() + " = " + value + "; ");
      }
    }
    staticConstructorClone.insertBefore(str.toString());

    ctClass.addConstructor(staticConstructorClone);

    return Outcome.ACCEPT;
  }
}
