package com.gzoltar.core.instr.matchers;

import static org.junit.Assert.assertTrue;
import org.gzoltar.examples.AbstractClass;
import org.gzoltar.examples.InterfaceClass;
import org.gzoltar.examples.PublicModifiers;
import org.junit.Test;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class TestNameMatcher {

  private final static ClassPool pool = ClassPool.getDefault();

  @Test
  public void testClassNameMatcher() throws Exception {
    ClassNameMatcher classNameMatcher = new ClassNameMatcher("org.*.Abstract*");
    CtClass ctClass = pool.get(AbstractClass.class.getCanonicalName());
    assertTrue(classNameMatcher.matches(ctClass));
  }

  @Test
  public void testMethodNameMatcher() throws Exception {
    MethodNameMatcher methodNameMatcher = new MethodNameMatcher("*Negative*");
    CtClass ctClass = pool.get(InterfaceClass.class.getCanonicalName());
    CtBehavior ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertTrue(methodNameMatcher.matches(ctBehavior));
  }

  @Test
  public void testFieldNameMatcher() throws Exception {
    FieldNameMatcher fieldNameMatcher = new FieldNameMatcher("s*g");
    CtClass ctClass = pool.get(PublicModifiers.class.getCanonicalName());
    CtField ctField = ctClass.getField("string");
    assertTrue(fieldNameMatcher.matches(ctField));
  }

}
