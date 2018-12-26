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
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.analysis.Type;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class ClinitPass implements IPass {

  private final InstrumentationLevel instrumentationLevel;

  public ClinitPass(final InstrumentationLevel instrumentationLevel) {
    this.instrumentationLevel = instrumentationLevel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(final CtClass ctClass, final String ctClassHash) throws Exception {
    if (this.instrumentationLevel == InstrumentationLevel.NONE) {
      return Outcome.REJECT;
    }

    if (ctClass.isInterface()) {
      // TODO add support for interfaces
      return Outcome.REJECT;
    }

    CtConstructor clinit = ctClass.makeClassInitializer();
    if (clinit != null) {
      // clone <clinit> method
      if (this.transform(ctClass, clinit) == Outcome.REJECT) {
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
  public Outcome transform(final CtClass ctClass, final CtBehavior clinit) throws Exception {
    CtConstructor staticConstructorClone = new CtConstructor((CtConstructor) clinit, ctClass, null);
    staticConstructorClone.getMethodInfo().setName("$_clinit_clone_"); // FIXME hardcoded string
    staticConstructorClone.setModifiers(AccessFlag.PRIVATE | AccessFlag.STATIC); // FIXME hardcoded access

    // reset static non-final fields to their default values
    StringBuilder str = new StringBuilder();
    for (CtField ctField : ctClass.getDeclaredFields()) {
      if (!Modifier.isStatic(ctField.getModifiers()) || Modifier.isFinal(ctField.getModifiers())) {
        // skip static final fields of being reset, i.e., only static non-final fields are reset
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

    // remove 'final' access of all static write fields
    staticConstructorClone.instrument(new ExprEditor() {
      @Override
      public void edit(FieldAccess fieldAccess) throws CannotCompileException {
        try {
          if (fieldAccess.isStatic() && fieldAccess.isWriter()) {
            final CtField field = fieldAccess.getField();
            if (field.getName().equals("serialVersionUID")) {
              // We must not remove final from serialVersionUID or else the class cannot be
              // serialised and de-serialised any more
              return;
            }

            //fieldAccess.replace("{ $_ = $proceed($$); }");

            if (Modifier.isFinal(field.getModifiers())) {
              // keep all modified flags other than FINAL
              field.setModifiers(field.getModifiers() & ~Modifier.FINAL);
            }
          }
        } catch (NotFoundException e) {
          throw new CannotCompileException(e);
        }
      }
    });

    return Outcome.ACCEPT;
  }
}
