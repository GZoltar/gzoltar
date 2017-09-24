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
