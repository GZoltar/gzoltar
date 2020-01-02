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
import org.gzoltar.examples.DeprecatedAnnotation;
import org.junit.Test;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

@SuppressWarnings("deprecation")
public class TestAnnotationMatcher {

  private final static ClassPool pool = ClassPool.getDefault();

  @Test
  public void testClassDeprecatedMatcher() throws Exception {
    ClassAnnotationMatcher classAnnotationMatcher =
        new ClassAnnotationMatcher(Deprecated.class.getCanonicalName());
    CtClass ctClass = pool.get(DeprecatedAnnotation.class.getCanonicalName());
    assertTrue(classAnnotationMatcher.matches(ctClass));
  }

  @Test
  public void testMethodDeprecatedMatcher() throws Exception {
    MethodAnnotationMatcher methodAnnotationMatcher =
        new MethodAnnotationMatcher(Deprecated.class.getCanonicalName());
    CtClass ctClass = pool.get(DeprecatedAnnotation.class.getCanonicalName());
    CtBehavior ctBehavior = ctClass.getDeclaredMethod("deprecatedMethod");
    assertTrue(methodAnnotationMatcher.matches(ctBehavior));
  }

  @Test
  public void testFieldDeprecatedMatcher() throws Exception {
    FieldAnnotationMatcher fieldAnnotationMatcher =
        new FieldAnnotationMatcher(Deprecated.class.getCanonicalName());
    CtClass ctClass = pool.get(DeprecatedAnnotation.class.getCanonicalName());
    CtField ctField = ctClass.getField("deprecatedField");
    assertTrue(fieldAnnotationMatcher.matches(ctField));
  }

}
