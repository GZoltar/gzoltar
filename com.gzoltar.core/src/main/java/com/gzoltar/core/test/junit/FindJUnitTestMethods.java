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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import org.jacoco.core.runtime.WildcardMatcher;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.jupiter.engine.JupiterTestEngine;
import com.gzoltar.core.util.ClassType;
import com.gzoltar.core.listeners.Listener;
import com.gzoltar.core.test.TestMethod;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import org.junit.vintage.engine.VintageTestEngine;
import org.junit.platform.launcher.listeners.UniqueIdTrackingListener;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.io.TempDirFactory;
import static org.junit.platform.commons.support.ReflectionSupport.streamNestedClasses;
public final class FindJUnitTestMethods {

  /**
   *
   * @param testsMatcher
   * @param testClassName
   * @return
   */
  public static List<TestMethod> find(final WildcardMatcher testsMatcher,
                                      final String testClassName) throws ClassNotFoundException {
    System.out.println("dsnmfjnfdnj");
    //I think that this prevents the optimization of needed dependencies
    VintageTestEngine vintageTestEngine = new VintageTestEngine();
    JupiterTestEngine JupiterTestEngine = new JupiterTestEngine();
    System.out.println("sddfbgdf" + testClassName);
    UniqueIdTrackingListener a = new UniqueIdTrackingListener();
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
      System.out.println(test.getDisplayName());
      System.out.println(test.getUniqueId());
      if (test.isTest()){
        System.out.println(test.getDisplayName());
        testsList.add(new TestMethod(ClassType.JUNIT,classname + Listener.TEST_CLASS_NAME_SEPARATOR + test.getDisplayName()));
      }else{
        testsList.addAll(getTests(testPlan,testPlan.getChildren(test),classname));
      }
    }

    return testsList;
  }
}
