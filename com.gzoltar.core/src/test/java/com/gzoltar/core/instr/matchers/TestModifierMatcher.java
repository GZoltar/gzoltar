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
package com.gzoltar.core.instr.matchers;

import static org.junit.Assert.assertTrue;
import org.gzoltar.examples.AbstractClass;
import org.gzoltar.examples.EnumClass;
import org.gzoltar.examples.InterfaceClass;
import org.gzoltar.examples.PrivateModifiers;
import org.gzoltar.examples.ProtectedModifiers;
import org.gzoltar.examples.PublicFinalModifiers;
import org.gzoltar.examples.PublicModifiers;
import org.junit.Test;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.AccessFlag;

public class TestModifierMatcher {

  private final static ClassPool pool = ClassPool.getDefault();

  @Test
  public void testPublicClassModifier() throws Exception {
    ClassModifierMatcher classModifierMatcher = new ClassModifierMatcher(AccessFlag.PUBLIC);
    CtClass ctClass = pool.get(PublicModifiers.class.getCanonicalName());
    assertTrue(classModifierMatcher.matches(ctClass));
  }

  @Test
  public void testPublicMethodModifier() throws Exception {
    MethodModifierMatcher methodModifierMatcher = new MethodModifierMatcher(AccessFlag.PUBLIC);
    CtClass ctClass = pool.get(PublicModifiers.class.getCanonicalName());
    CtBehavior ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertTrue(methodModifierMatcher.matches(ctBehavior));
  }

  @Test
  public void testPublicFieldModifier() throws Exception {
    FieldModifierMatcher fieldModifierMatcher = new FieldModifierMatcher(AccessFlag.PUBLIC);
    CtClass ctClass = pool.get(PublicModifiers.class.getCanonicalName());
    CtField ctField = ctClass.getField("string");
    assertTrue(fieldModifierMatcher.matches(ctField));
  }

  @Test
  public void testPrivateMethodModifier() throws Exception {
    MethodModifierMatcher methodModifierMatcher = new MethodModifierMatcher(AccessFlag.PRIVATE);
    CtClass ctClass = pool.get(PrivateModifiers.class.getCanonicalName());
    CtBehavior ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertTrue(methodModifierMatcher.matches(ctBehavior));
  }

  @Test
  public void testPrivateFieldModifier() throws Exception {
    FieldModifierMatcher fieldModifierMatcher = new FieldModifierMatcher(AccessFlag.PRIVATE);
    CtClass ctClass = pool.get(PrivateModifiers.class.getCanonicalName());
    CtField ctField = ctClass.getField("string");
    assertTrue(fieldModifierMatcher.matches(ctField));
  }

  @Test
  public void testPublicFinalClassModifier() throws Exception {
    ClassModifierMatcher classModifierMatcher = new ClassModifierMatcher(AccessFlag.FINAL);
    CtClass ctClass = pool.get(PublicFinalModifiers.class.getCanonicalName());
    assertTrue(classModifierMatcher.matches(ctClass));
  }

  @Test
  public void testPublicFinalMethodModifier() throws Exception {
    MethodModifierMatcher methodModifierMatcher = new MethodModifierMatcher(AccessFlag.FINAL);
    CtClass ctClass = pool.get(PublicFinalModifiers.class.getCanonicalName());
    CtBehavior ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertTrue(methodModifierMatcher.matches(ctBehavior));
  }

  @Test
  public void testPublicFinalFieldModifier() throws Exception {
    FieldModifierMatcher fieldModifierMatcher = new FieldModifierMatcher(AccessFlag.FINAL);
    CtClass ctClass = pool.get(PublicFinalModifiers.class.getCanonicalName());
    CtField ctField = ctClass.getField("string");
    assertTrue(fieldModifierMatcher.matches(ctField));
  }

  @Test
  public void testProtectedMethodModifier() throws Exception {
    MethodModifierMatcher methodModifierMatcher = new MethodModifierMatcher(AccessFlag.PROTECTED);
    CtClass ctClass = pool.get(ProtectedModifiers.class.getCanonicalName());
    CtBehavior ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertTrue(methodModifierMatcher.matches(ctBehavior));
  }

  @Test
  public void testProtectedFieldModifier() throws Exception {
    FieldModifierMatcher fieldModifierMatcher = new FieldModifierMatcher(AccessFlag.PROTECTED);
    CtClass ctClass = pool.get(ProtectedModifiers.class.getCanonicalName());
    CtField ctField = ctClass.getField("string");
    assertTrue(fieldModifierMatcher.matches(ctField));
  }

  @Test
  public void testAbstractClassModifier() throws Exception {
    ClassModifierMatcher classModifierMatcher = new ClassModifierMatcher(AccessFlag.ABSTRACT);
    CtClass ctClass = pool.get(AbstractClass.class.getCanonicalName());
    assertTrue(classModifierMatcher.matches(ctClass));
  }

  @Test
  public void testAbstractMethodModifier() throws Exception {
    MethodModifierMatcher methodModifierMatcher = new MethodModifierMatcher(AccessFlag.ABSTRACT);
    CtClass ctClass = pool.get(AbstractClass.class.getCanonicalName());
    CtBehavior ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertTrue(methodModifierMatcher.matches(ctBehavior));
  }

  @Test
  public void testEnumClassModifier() throws Exception {
    ClassModifierMatcher classModifierMatcher = new ClassModifierMatcher(AccessFlag.ENUM);
    CtClass ctClass = pool.get(EnumClass.class.getCanonicalName());
    assertTrue(classModifierMatcher.matches(ctClass));
  }

  @Test
  public void testInterfaceModifier() throws Exception {
    ClassModifierMatcher classModifierMatcher = new ClassModifierMatcher(AccessFlag.INTERFACE);
    CtClass ctClass = pool.get(InterfaceClass.class.getCanonicalName());
    assertTrue(classModifierMatcher.matches(ctClass));
  }

}
