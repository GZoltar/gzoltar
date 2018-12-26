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
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.filter.DuplicateFilter;
import com.gzoltar.core.instr.filter.EmptyMethodFilter;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.AccessFlag;

public class ResetPass implements IPass {

  private static final String fieldStr = "boolean[] $gzoltarResetFlag = null;";

  protected static final String ARRAY_OBJECT_NAME = "$tmpFlag";

  private static final String call = InstrumentationConstants.SYSTEM_CLASS_NAME_JVM + "."
      + InstrumentationConstants.SYSTEM_CLASS_FIELD_NAME + ".equals(" + ARRAY_OBJECT_NAME + "); ";

  private static final DuplicateFilter duplicateFilter = new DuplicateFilter("$gzoltarResetter", null);

  private final EmptyMethodFilter emptyMethodFilter = new EmptyMethodFilter();

  private final InstrumentationLevel instrumentationLevel;

  public ResetPass(final InstrumentationLevel instrumentationLevel) {
    this.instrumentationLevel = instrumentationLevel;
  }

  public static Outcome makeEmptyResetter(final CtClass ctClass) throws Exception {
    if (ctClass.isInterface()) {
      return Outcome.REJECT;
    }

    // avoid adding the resetter method more than once
    if (duplicateFilter.filter(ctClass) == Outcome.REJECT) {
      return Outcome.REJECT;
    }

    CtMethod gzoltarResetter = CtMethod.make("void $gzoltarResetter() { }", ctClass); // TODO
    gzoltarResetter.setModifiers(AccessFlag.PUBLIC | AccessFlag.STATIC | AccessFlag.SYNCHRONIZED | AccessFlag.SYNTHETIC); // TODO
    ctClass.addMethod(gzoltarResetter);
    return Outcome.ACCEPT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(final CtClass ctClass, final String ctClassHash) throws Exception {
    if (this.instrumentationLevel == InstrumentationLevel.NONE) {
      return Outcome.REJECT;
    }

    // if there is not a method called '$_clinit_clone_' no need to do anything!
    boolean foundClinitClone = false;
    for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
      if (ctBehavior.getLongName().equals(ctClass.getName() + ".<clinit>()")) { // FIXME hardcoded string
        foundClinitClone = true;
        break;
      }
    }
    if (!foundClinitClone) {
      return Outcome.REJECT;
    }

    CtConstructor clinit = ctClass.getClassInitializer();
    if (clinit != null) {
      // inject field
      CtField f = CtField.make(fieldStr, ctClass);
      f.setModifiers(f.getModifiers() | AccessFlag.PRIVATE | AccessFlag.STATIC | AccessFlag.SYNTHETIC | AccessFlag.TRANSIENT); // TODO
      ctClass.addField(f);

      CtField f2 = CtField.make("java.util.Properties $gzoltarDefaultProperties = null;", ctClass);
      f2.setModifiers(f2.getModifiers() | AccessFlag.PRIVATE | AccessFlag.STATIC | AccessFlag.SYNTHETIC | AccessFlag.TRANSIENT); // TODO
      ctClass.addField(f2);

      // inject code in the resetter method
      CtMethod gzoltarResetter = ctClass.getMethod("$gzoltarResetter", "()V"); // FIXME
      gzoltarResetter.setBody("{ "
            + "if ($gzoltarResetFlag == null) { "
              + "Object[] $tmpFlag = new Object[] { \"" + ctClassHash + "\" }; "
              + call + " "
              + "$gzoltarResetFlag = (boolean[]) $tmpFlag[0]; "
              + "$gzoltarDefaultProperties = (java.util.Properties) java.lang.System.getProperties().clone(); "
            + "} "
            + "if ($gzoltarResetFlag[0] == false) { "
               + "return; "
            + "} "
            + "$gzoltarResetFlag[0] = false; "
            + "java.lang.System.setProperties((java.util.Properties) $gzoltarDefaultProperties.clone()); "
            + "$_clinit_clone_(); "
          + "}");

      // replace body of original <clinit> method (which should at this stage point to '$_clinit_clone_();')
      // with a call to our resetter
      clinit.setBody("{ $gzoltarResetter(); }"); // FIXME hardcoded string

      for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
        if (ctBehavior.getName().equals("$gzoltarResetter")) {
          // for obvious reasons, resetter method cannot call itself
          continue;
        }
        if (ctBehavior.getLongName().equals(ctClass.getName() + ".<clinit>()")) { // FIXME hardcoded string
          // skip our $_clinit_clone_();
          continue;
        }
        if (ctBehavior.getMethodInfo().isStaticInitializer() || ctBehavior.getName().equals("<clinit>")) {
          // skip the original <clinit> method as it has been handled
          continue;
        }

        if (this.emptyMethodFilter.filter(ctBehavior) == Outcome.REJECT) {
          // skip empty methods
          continue;
        }

        this.transform(ctClass, ctBehavior);
      }

      return Outcome.ACCEPT;
    }

    return Outcome.REJECT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(final CtClass ctClass, final CtBehavior ctBehavior) throws Exception {
    ctBehavior.insertBefore("$gzoltarResetter();");
    return Outcome.ACCEPT;
  }
}
