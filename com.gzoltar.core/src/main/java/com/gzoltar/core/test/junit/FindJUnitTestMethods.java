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

import com.gzoltar.core.listeners.Listener;
import com.gzoltar.core.test.TestMethod;
import com.gzoltar.core.util.ClassType;
import org.jacoco.core.runtime.WildcardMatcher;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
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

    return new ArrayList<>(getTests(testPlan, testPlan.getRoots(),testClassName));
  }

  public static Collection<TestMethod> getTests(TestPlan testPlan, Set<TestIdentifier> roots,String classname){
    List<TestMethod> testsList = new ArrayList<>();

    for (TestIdentifier test: roots){
      if (test.isTest()){
        testsList.add(new TestMethod(ClassType.JUNIT,classname + Listener.TEST_CLASS_NAME_SEPARATOR + test.getDisplayName()));
      }else{
        testsList.addAll(getTests(testPlan,testPlan.getChildren(test),classname));
      }
    }

    return testsList;
  }
}
