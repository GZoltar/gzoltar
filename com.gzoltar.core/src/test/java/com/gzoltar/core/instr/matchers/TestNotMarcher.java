package com.gzoltar.core.instr.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.gzoltar.examples.DeprecatedAnnotation;
import org.junit.Test;
import javassist.ClassPool;
import javassist.CtClass;

@SuppressWarnings("deprecation")
public class TestNotMarcher {

  private final static ClassPool pool = ClassPool.getDefault();

  @Test
  public void testAllMethodsExceptDeprecatedOnes() throws Exception {
    MethodAnnotationMatcher methodsExceptDeprecatedOnes =
        new MethodAnnotationMatcher(Deprecated.class.getCanonicalName());
    NotMatcher notMatcher = new NotMatcher(methodsExceptDeprecatedOnes);
    CtClass ctClass = pool.get(DeprecatedAnnotation.class.getCanonicalName());
    assertFalse(notMatcher.matches(ctClass.getDeclaredMethod("deprecatedMethod")));
    assertTrue(notMatcher.matches(ctClass.getDeclaredMethod("notDeprecatedMethod")));
  }

}
