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

public class TestPrefixMatcher {

  private final static ClassPool pool = ClassPool.getDefault();

  @Test
  public void testClassPrefixMatcher() throws Exception {
    PrefixMatcher prefixMatcher = new PrefixMatcher("org.gzoltar.examples.Abstract");
    CtClass ctClass = pool.get(AbstractClass.class.getCanonicalName());
    assertTrue(prefixMatcher.matches(ctClass));
  }

  @Test
  public void testMethodPrefixMatcher() throws Exception {
    PrefixMatcher prefixMatcher = new PrefixMatcher("is");
    CtClass ctClass = pool.get(InterfaceClass.class.getCanonicalName());
    CtBehavior ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertTrue(prefixMatcher.matches(ctBehavior));
  }

  @Test
  public void testFieldPrefixMatcher() throws Exception {
    PrefixMatcher prefixMatcher = new PrefixMatcher("str");
    CtClass ctClass = pool.get(PublicModifiers.class.getCanonicalName());
    CtField ctField = ctClass.getField("string");
    assertTrue(prefixMatcher.matches(ctField));
  }

}
