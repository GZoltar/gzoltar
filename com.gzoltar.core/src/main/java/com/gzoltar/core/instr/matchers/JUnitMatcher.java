package com.gzoltar.core.instr.matchers;

import java.lang.reflect.Modifier;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class JUnitMatcher implements IMatcher {

  public final IMatcher matcher;

  public JUnitMatcher() {
    this.matcher = new AndMatcher(
        // a JUnit test class must be public
        new ClassModifierMatcher(Modifier.PUBLIC),
        // a JUnit test class cannot be an abstract class
        new NotMatcher(new ClassModifierMatcher(Modifier.ABSTRACT)),
        // a JUnit test class cannot be an interface class
        new NotMatcher(new ClassModifierMatcher(Modifier.INTERFACE)),
        // a JUnit test class cannot be an anonymous class
        new NotMatcher(new AnonymousMatcher()),
        // a JUnit3/4 test class must has ...
        new OrMatcher(
            // a JUnit3 test class must has a specific super class
            new SuperclassMatcher("junit.framework.TestCase"),
            // a JUnit4 test class must has at least one method annotated with specific JUnit tags
            new OrMatcher(new MethodAnnotationMatcher("org.junit.Test"),
                new MethodAnnotationMatcher("org.junit.experimental.theories.Theory"))));
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    return this.matcher.matches(ctClass);
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return this.matches(ctBehavior.getDeclaringClass());
  }

  @Override
  public boolean matches(final CtField ctField) {
    return this.matches(ctField.getDeclaringClass());
  }
}
