package com.gzoltar.core.instr.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.gzoltar.examples.tests.AbstractTestClass;
import org.gzoltar.examples.tests.ChildTestClassWithTestCases;
import org.gzoltar.examples.tests.ChildTestClassWithoutTestCases;
import org.gzoltar.examples.tests.JUnitClassWithInnerClass;
import org.gzoltar.examples.tests.JUnitClassWithInnerClass.SomeStaticInnerClass;
import org.junit.Test;
import javassist.ClassPool;
import javassist.CtClass;

public class TestJUnitMatcher {

  private final static ClassPool pool = ClassPool.getDefault();

  @Test
  public void testAbstractClass() throws Exception {
    JUnitMatcher matcher = new JUnitMatcher();
    CtClass ctClass = pool.get(AbstractTestClass.class.getName());
    assertFalse(matcher.matches(ctClass));
  }

  @Test
  public void testChildTestClassWithoutTestCases() throws Exception {
    JUnitMatcher matcher = new JUnitMatcher();
    CtClass ctClass = pool.get(ChildTestClassWithoutTestCases.class.getName());
    assertTrue(matcher.matches(ctClass));
  }

  @Test
  public void testChildTestClassWithTestCases() throws Exception {
    JUnitMatcher matcher = new JUnitMatcher();
    CtClass ctClass = pool.get(ChildTestClassWithTestCases.class.getName());
    assertTrue(matcher.matches(ctClass));
  }

  @Test
  public void testJUnitClassWithInnerClass() throws Exception {
    JUnitMatcher matcher = new JUnitMatcher();
    CtClass ctClass = pool.get(JUnitClassWithInnerClass.class.getName());
    assertTrue(matcher.matches(ctClass));
    ctClass = pool.get(SomeStaticInnerClass.class.getName());
    assertTrue(matcher.matches(ctClass));
  }
}
