package com.gzoltar.cli.test.junit;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.runner.Description;
import org.junit.runner.Request;
import com.gzoltar.core.listeners.Listener;

public final class FindJUnitAtomicTests {

  /**
   * 
   * @param testClass
   * @return
   */
  public static List<String> find(final String testClass) {
    final List<String> unitTestCases = new ArrayList<String>();

    // load the test class using a default classloader
    Class<?> clazz = null;
    try {
      clazz = Class.forName(testClass, false, ClassLoader.getSystemClassLoader());
    } catch (ClassNotFoundException e) {
      System.err.println("Class " + testClass + " not found.\n" + e);
      return unitTestCases;
    }

    assert clazz != null;

    // get atomic test cases

    Map<String, Set<String>> methods = new LinkedHashMap<String, Set<String>>();
    for (Description test : Request.aClass(clazz).getRunner().getDescription().getChildren()) {
      // a parameterized atomic test case does not have a method name
      if (test.getMethodName() == null) {
        for (Method m : clazz.getMethods()) {
          // JUnit 3: an atomic test case is "public", does not return anything ("void"), has 0
          // parameters and starts with the word "test"
          // JUnit 4: an atomic test case is annotated with @Test
          if (m.isAnnotationPresent(org.junit.Test.class)
              || Modifier.isPublic(m.getModifiers()) && m.getReturnType().equals(Void.TYPE)
                  && m.getParameterTypes().length == 0 && m.getName().startsWith("test")) {
            Set<String> ms = (methods.containsKey(testClass) ? methods.get(testClass)
                : new LinkedHashSet<String>());
            ms.add(m.getName() + test.getDisplayName());
            methods.put(testClass, ms);
          }
        }
      } else {
        // non-parameterized atomic test case
        Set<String> ms =
            (methods.containsKey(test.getClassName()) ? methods.get(test.getClassName())
                : new LinkedHashSet<String>());
        ms.add(test.getMethodName());
        methods.put(test.getClassName(), ms);
      }
    }

    for (final String className : methods.keySet()) {
      for (final String methodName : methods.get(className)) {
        unitTestCases.add(className + Listener.TEST_CLASS_NAME_SEPARATOR + methodName);
      }
    }

    return unitTestCases;
  }
}
