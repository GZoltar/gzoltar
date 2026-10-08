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
package com.gzoltar.core.test.junit;

import java.util.ArrayList;
import java.util.List;
import org.jacoco.core.runtime.WildcardMatcher;
import com.gzoltar.core.util.ClassType;
import com.gzoltar.core.listeners.Listener;
import com.gzoltar.core.test.TestMethod;
// importing the new Launcher API libraries
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;


public final class FindJUnitTestMethods {

  /**
   * 
   * @param testsMatcher
   * @param testClassName
   * @return
   */
  public static List<TestMethod> find(final WildcardMatcher testsMatcher,
      final String testClassName) throws ClassNotFoundException {
    final List<TestMethod> testMethods = new ArrayList<TestMethod>();

    // load the test class using a default classloader
    Class<?> clazz =
        Class.forName(testClassName, false, Thread.currentThread().getContextClassLoader());
    assert clazz != null;
    // creating a request to find the tests inside this class.
    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
        .selectors(DiscoverySelectors.selectClass(clazz))
        .build();
    // discover tests using the JUnit Platform Launcher to build a test plan.
    Launcher launcher = LauncherFactory.create();
    TestPlan testPlan = launcher.discover(request);
    
    // iterate through all nodes in the test plan.
    for (TestIdentifier root : testPlan.getRoots()) {
      for (TestIdentifier test : testPlan.getDescendants(root)) {
        
        // checking containers, keep only actual test methods
        if (test.isTest()) {
          test.getSource().ifPresent(source -> {
            
            // ensure the test source is a Java method
            if (source instanceof MethodSource) {
              MethodSource methodSource = (MethodSource) source;
              
              // building the standard GZoltar format: ClassName#methodName
              String testMethodFullName = methodSource.getClassName() 
                  + Listener.TEST_CLASS_NAME_SEPARATOR + methodSource.getMethodName();

              // if the display name differs from the method name, append it
              if (!methodSource.getMethodName().equals(test.getDisplayName())) {
                testMethodFullName += test.getDisplayName();
              }
              // Add to the list if the generated name matches the provided GZoltar wildcard matcher
              if (testsMatcher.matches(testMethodFullName)) {
                testMethods.add(new TestMethod(ClassType.JUNIT, testMethodFullName));
              }
            }
          });
        }
      }
    }
    return testMethods;
  }
    // JUnit 6 doesn't need manual controls. So removed looksLikeTest.
}
