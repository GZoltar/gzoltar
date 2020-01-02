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
package com.gzoltar.core.instr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;
import com.gzoltar.core.runtime.Collector;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

/**
 * This class adds a new static field to a bootstrap class that will be used by instrumented
 * classes. As the system class itself needs to be instrumented this instrumenter requires a Java
 * agent.
 */
public final class SystemClassInstrumenter {

  /**
   * Adds a new static field to a bootstrap class that will be used by instrumented classes.
   * 
   * @param inst
   * @param className
   * @param accessFieldName
   * @throws Exception
   */
  public static void instrumentSystemClass(final Instrumentation inst, final String className,
      final String accessFieldName) throws Exception {

    final ClassPool cp = ClassPool.getDefault();
    final ClassFileTransformer transformer = new ClassFileTransformer() {
      public byte[] transform(final ClassLoader loader, final String name,
          final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain,
          final byte[] source) throws IllegalClassFormatException {
        if (name.equals(className)) {
          try {
            final CtClass cc = cp.makeClass(new ByteArrayInputStream(source));
            byte[] bytes = instrument(cc, accessFieldName);
            return bytes;
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        return null;
      }
    };

    // enable instrumentation
    inst.addTransformer(transformer);
    // load system class
    final Class<?> clazz = Class.forName(InstrumentationConstants.SYSTEM_CLASS_NAME_JVM);
    // disable instrumentation
    inst.removeTransformer(transformer);

    // setup field to use GZoltar's collector
    try {
      // has the new field been added?
      final Field field = clazz.getField(accessFieldName);
      // point field to GZoltar' runtime collector
      field.set(null, Collector.instance());
    } catch (final NoSuchFieldException e) {
      throw new RuntimeException("Class '" + className + "' could not be instrumented.", e);
    }
  }

  private static byte[] instrument(final CtClass ctClass, final String accessFieldName)
      throws IOException, CannotCompileException {

    CtField f = CtField.make(InstrumentationConstants.SYSTEM_CLASS_FIELD_DESC + accessFieldName
        + InstrumentationConstants.EOL, ctClass);
    f.setModifiers(f.getModifiers() | InstrumentationConstants.SYSTEM_CLASS_FIELD_ACC);
    ctClass.addField(f);

    CtConstructor clinit = ctClass.makeClassInitializer();
    clinit.instrument(new ExprEditor() {
      @Override
      public void edit(FieldAccess f) throws CannotCompileException {
        if (f.getFieldName().equals(accessFieldName)) {
          f.replace("{ $_ = $proceed($$); }");
        }
      }
    });

    return ctClass.toBytecode();
  }
}
