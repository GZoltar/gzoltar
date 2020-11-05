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

import java.lang.reflect.Modifier;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class JUnitMatcher implements IMatcher {

  public final IMatcher matcher;

  public JUnitMatcher() {
    this.matcher = new AndMatcher(
        // a JUnit test class must be public or 'default' package access
        new OrMatcher(new ClassModifierMatcher(Modifier.PUBLIC), new ClassModifierMatcher(0)),
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
                new MethodAnnotationMatcher("org.junit.experimental.theories.Theory")),
            // a JUnit5 test class must has at least one method annotated with specific JUnit tags
            new MethodAnnotationMatcher("org.junit.jupiter.api.Test")));
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
