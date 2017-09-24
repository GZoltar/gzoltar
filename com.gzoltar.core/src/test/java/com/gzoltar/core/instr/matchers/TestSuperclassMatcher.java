package com.gzoltar.core.instr.matchers;

import static org.junit.Assert.assertTrue;
import org.gzoltar.examples.PublicModifiers;
import org.junit.Test;
import javassist.ClassPool;
import javassist.CtClass;

public class TestSuperclassMatcher {

  private final static ClassPool pool = ClassPool.getDefault();

  @Test
  public void testSuperclassMatcher() throws Exception {
    SuperclassMatcher superclassMatcher = new SuperclassMatcher("org.gzoltar.*.Abstract*");
    CtClass ctClass = pool.get(PublicModifiers.class.getCanonicalName());
    assertTrue(superclassMatcher.matches(ctClass));
  }

}
