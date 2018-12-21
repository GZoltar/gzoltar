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

    CtConstructor clinit = ctClass.getClassInitializer();
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
