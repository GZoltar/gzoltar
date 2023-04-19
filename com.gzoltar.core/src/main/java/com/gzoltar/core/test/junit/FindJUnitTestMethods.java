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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLOutput;
import java.util.*;

import javassist.CtClass;
import javassist.CtMethod;
import org.jacoco.core.runtime.WildcardMatcher;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import com.gzoltar.core.util.ClassType;
import com.gzoltar.core.listeners.Listener;
import com.gzoltar.core.test.TestMethod;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

import org.junit.vintage.engine.VintageTestEngine;
import org.junit.vintage.engine.discovery.VintageDiscoverer;
import org.junit.vintage.engine.support.UniqueIdReader;
import org.junit.vintage.engine.descriptor.VintageTestDescriptor;




public final class FindJUnitTestMethods {

  /**
   *
   * @param testsMatcher
   * @param testClassName
   * @return
   */
  public static List<TestMethod> find(final WildcardMatcher testsMatcher,
                                      final String testClassName) throws ClassNotFoundException {

    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(
                    selectClass(testClassName)
            ).build();

    Launcher launcher = LauncherFactory.create();

    TestPlan testPlan = launcher.discover(request);

    return new ArrayList<>(getTests(testPlan, testPlan.getRoots()));


  }

  public static Collection<TestMethod> getTests(TestPlan testPlan, Set<TestIdentifier> roots){
    List<TestMethod> testsList = new ArrayList<>();

    for (TestIdentifier test: roots){
      if (test.isTest()){
        testsList.add(new TestMethod(ClassType.JUNIT,testPlan.getClass().getName() + Listener.TEST_CLASS_NAME_SEPARATOR + test.getDisplayName()));
      }else{
        testsList.addAll(getTests(testPlan,testPlan.getChildren(test)));
      }
    }

    return testsList;
  }

  private static boolean looksLikeTest(final Method m) {
    // JUnit 3: an atomic test case is "public", does not return anything ("void"), has 0
    // parameters and starts with the word "test"
    // JUnit 4: an atomic test case is annotated with @Test
    return (m.isAnnotationPresent(org.junit.Test.class) || m.isAnnotationPresent(org.junit.jupiter.api.Test.class)|| (m.getParameterTypes().length == 0
            && m.getReturnType().equals(Void.TYPE) && Modifier.isPublic(m.getModifiers())
            && (m.getName().startsWith("test") || m.getName().endsWith("Test")
            || m.getName().startsWith("Test") || m.getName().endsWith("test"))));
  }
}
