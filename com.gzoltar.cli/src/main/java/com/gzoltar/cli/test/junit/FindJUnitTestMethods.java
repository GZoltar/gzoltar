/**
 * Copyright (C) 2018 GZoltar contributors.
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
package com.gzoltar.cli.test.junit;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.Request;
import com.gzoltar.cli.test.TestMethod;

public final class FindJUnitTestMethods {

  /**
   * 
   * @param testClassName
   * @return
   */
  public static List<TestMethod> find(final String testClassName) throws ClassNotFoundException {
    final List<TestMethod> testMethods = new ArrayList<TestMethod>();

    // load the test class using a default classloader
    Class<?> clazz =
        Class.forName(testClassName, false, Thread.currentThread().getContextClassLoader());
    assert clazz != null;

    for (Description test : Request.aClass(clazz).getRunner().getDescription().getChildren()) {
      // a parameterised atomic test case does not have a method name
      if (test.getMethodName() == null) {
        for (Method m : clazz.getMethods()) {
          if (looksLikeTest(m)) {
            testMethods.add(new TestMethod(clazz.getName(), m.getName() + test.getDisplayName()));
          }
        }
      } else {
        // non-parameterised atomic test case
        testMethods.add(new TestMethod(test.getTestClass().getName(), test.getMethodName()));
      }
    }

    return testMethods;
  }

  private static boolean looksLikeTest(final Method m) {
    // JUnit 3: an atomic test case is "public", does not return anything ("void"), has 0
    // parameters and starts with the word "test"
    // JUnit 4: an atomic test case is annotated with @Test
    return (m.isAnnotationPresent(org.junit.Test.class) || (m.getParameterTypes().length == 0
        && m.getReturnType().equals(Void.TYPE) && Modifier.isPublic(m.getModifiers())
        && (m.getName().startsWith("test") || m.getName().endsWith("Test")
            || m.getName().startsWith("Test") || m.getName().endsWith("test"))));
  }
}
