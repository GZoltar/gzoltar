package com.gzoltar.core.instr.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.gzoltar.examples.PrivateModifiers;
import org.gzoltar.examples.ProtectedModifiers;
import org.gzoltar.examples.PublicFinalModifiers;
import org.gzoltar.examples.PublicModifiers;
import org.junit.Test;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.AccessFlag;

public class TestAndMatcher {

  private final static ClassPool pool = ClassPool.getDefault();

  @Test
  public void testPublicAndFinalMethods() throws Exception {
    MethodModifierMatcher publicMethodsMatcher = new MethodModifierMatcher(AccessFlag.PUBLIC);
    MethodModifierMatcher protectedMethodsMatcher = new MethodModifierMatcher(AccessFlag.FINAL);
    AndMatcher orMatcher = new AndMatcher(publicMethodsMatcher, protectedMethodsMatcher);

    CtClass ctClass = pool.get(PublicFinalModifiers.class.getCanonicalName());
    CtBehavior ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertTrue(orMatcher.matches(ctBehavior));

    ctClass = pool.get(PublicModifiers.class.getCanonicalName());
    ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertFalse(orMatcher.matches(ctBehavior));

    ctClass = pool.get(ProtectedModifiers.class.getCanonicalName());
    ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertFalse(orMatcher.matches(ctBehavior));

    ctClass = pool.get(PrivateModifiers.class.getCanonicalName());
    ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertFalse(orMatcher.matches(ctBehavior));
  }

}
