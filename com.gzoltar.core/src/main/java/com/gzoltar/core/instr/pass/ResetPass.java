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
import com.gzoltar.core.instr.filter.MethodNoBodyFilter;
import com.gzoltar.core.instr.filter.FieldWorthyToBeResetFilter;
import com.gzoltar.core.util.ClassUtils;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.MethodInfo;

public class ResetPass implements IPass {

  private static final String call = InstrumentationConstants.SYSTEM_CLASS_NAME_JVM + "."
      + InstrumentationConstants.SYSTEM_CLASS_FIELD_NAME + ".equals($tmpFlag); ";

  private static final FieldWorthyToBeResetFilter fieldWorthyToBeResetFilter =
      new FieldWorthyToBeResetFilter();

  private final MethodNoBodyFilter methodNoBodyFilter = new MethodNoBodyFilter();

  /**
   * Adds an empty method called $gzoltarReseter to a {@link javassist.CtClass} object to avoid
   * *any* java.lang.NoSuchMethodError when a static field is accessed (either write or read access)
   * by another class.
   * 
   * @param ctClass
   * @return
   * @throws Exception
   */
  public static Outcome makeEmptyResetter(final CtClass ctClass) throws Exception {
    if (ctClass.isInterface() && !ClassUtils.isInterfaceClassSupported(ctClass)) {
      return Outcome.REJECT;
    }

    // Is there at least one static field that must be reset?
    for (CtField ctField : ctClass.getDeclaredFields()) {
      if (fieldWorthyToBeResetFilter.filter(ctField) == Outcome.ACCEPT) {
        CtMethod gzoltarResetter = CtMethod.make(InstrumentationConstants.RESETTER_METHOD_DESC_HUMAN
            + InstrumentationConstants.RESETTER_METHOD_NAME_WITH_ARGS + " { }", ctClass);
        gzoltarResetter.setModifiers(InstrumentationConstants.RESETTER_METHOD_ACC);
        ctClass.addMethod(gzoltarResetter);
        return Outcome.ACCEPT;
      }
    }

    return Outcome.REJECT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(final ClassLoader loader, final CtClass ctClass,
      final String ctClassHash) throws Exception {
    if (ctClass.isInterface() && !ClassUtils.isInterfaceClassSupported(ctClass)) {
      return Outcome.REJECT;
    }

    // if there is not a method called '$_clinit_clone_' no need to do anything!
    boolean foundClinitClone = false;
    for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
      MethodInfo methodInfo = ctBehavior.getMethodInfo2();
      if (!methodInfo.isStaticInitializer() && methodInfo.isMethod()
          && ctBehavior.getLongName().equals(ctClass.getName() + ".<clinit>()")) {
        foundClinitClone = true;
        break;
      }
    }
    if (!foundClinitClone) {
      return Outcome.REJECT;
    }

    CtConstructor clinit = ctClass.getClassInitializer();
    if (clinit != null) {
      // inject reset flag field
      CtField resetFlagField = CtField.make(InstrumentationConstants.RESET_FIELD_DESC_HUMAN
          + InstrumentationConstants.RESET_FIELD_NAME + " = "
          + InstrumentationConstants.RESET_FIELD_INIT_VALUE + ";", ctClass);
      resetFlagField
          .setModifiers(ctClass.isInterface() ? InstrumentationConstants.RESET_FIELD_INTF_ACC
              : InstrumentationConstants.RESET_FIELD_ACC);
      ctClass.addField(resetFlagField);

      CtField javaPropertiesField =
          CtField.make("java.util.Properties $gzoltarDefaultProperties = null;", ctClass);
      javaPropertiesField
          .setModifiers(AccessFlag.STATIC | AccessFlag.SYNTHETIC | AccessFlag.TRANSIENT);
      if (ctClass.isInterface()) {
        javaPropertiesField.setModifiers(AccessFlag.PUBLIC | javaPropertiesField.getModifiers());
      } else {
        javaPropertiesField.setModifiers(AccessFlag.PRIVATE | javaPropertiesField.getModifiers());
      }
      ctClass.addField(javaPropertiesField);

      // inject code in the resetter method
      CtMethod gzoltarResetter = ctClass.getMethod(InstrumentationConstants.RESETTER_METHOD_NAME,
          InstrumentationConstants.RESETTER_METHOD_DESC);
      gzoltarResetter.setBody("{ "
            + "if (" + InstrumentationConstants.RESET_FIELD_NAME + " == null) { "
              + "Object[] $tmpFlag = new Object[] { \"" + (loader == null ? "" : loader.hashCode()) + "\", \"" + ctClassHash + "\" }; "
              + call + " "
              + InstrumentationConstants.RESET_FIELD_NAME + " = (boolean[]) $tmpFlag[0]; "
              + "$gzoltarDefaultProperties = (java.util.Properties) java.lang.System.getProperties().clone(); "
            + "} "
            + "if (" + InstrumentationConstants.RESET_FIELD_NAME + "[0] == false) { "
               + "return; "
            + "} "
            + InstrumentationConstants.RESET_FIELD_NAME + "[0] = false; "
            + "java.lang.System.setProperties((java.util.Properties) $gzoltarDefaultProperties.clone()); "
            + InstrumentationConstants.CLINIT_CLONE_METHOD_NAME_WITH_ARGS + "; "
          + "}");

      // replace body of original <clinit> method (which should at this stage point to '$_clinit_clone_();')
      // with a call to our resetter
      clinit.setBody("{ " + InstrumentationConstants.RESETTER_METHOD_NAME_WITH_ARGS + "; }");

      for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
        if (ctBehavior.getName().equals(InstrumentationConstants.RESETTER_METHOD_NAME)) {
          // for obvious reasons, resetter method cannot call itself
          continue;
        }
        if (ctBehavior.getLongName().equals(ctClass.getName() + ".<clinit>()")) {
          // skip GZoltar $_clinit_clone_() method
          continue;
        }
        if (ctBehavior.getMethodInfo().isStaticInitializer() || ctBehavior.getName().equals("<clinit>")) {
          // skip the original <clinit> method as it has been handled
          continue;
        }

        if (this.methodNoBodyFilter.filter(ctBehavior) == Outcome.REJECT) {
          // skip methods with no body
          continue;
        }

        this.transform(loader, ctClass, ctClassHash, ctBehavior);
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
      final String ctClassHash, final CtBehavior ctBehavior) throws Exception {
    ctBehavior.insertBefore(InstrumentationConstants.RESETTER_METHOD_NAME_WITH_ARGS + ";");
    return Outcome.ACCEPT;
  }
}
