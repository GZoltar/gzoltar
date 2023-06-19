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
package com.gzoltar.core.test.testng;

import java.util.ArrayList;
import java.util.List;
import org.junit.runner.notification.RunListener;
import org.testng.TestNG;
import com.gzoltar.core.test.TestMethod;
import com.gzoltar.core.test.TestTask;

public class TestNGTestTask extends TestTask {

  public TestNGTestTask(final boolean offline, final boolean collectCoverage,
                        final boolean initTestClass, final TestMethod testMethod) {
    super(offline, collectCoverage, initTestClass, testMethod);
  }

  /**
   * Callable method to run JUnit test and return result.
   * 
   * {@inheritDoc}
   */
  @SuppressWarnings("deprecation")
  @Override
  public TestNGTestResult call() throws Exception {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Class<?> clazz = this.initTestClass ? Class.forName(this.testMethod.getTestClassName())
        : Class.forName(this.testMethod.getTestClassName(), false, classLoader);

    List<String> testMethod = new ArrayList<String>();
    testMethod.add(this.testMethod.getTestMethodName());

    TestNG runner = new TestNG();
    runner.setTestClasses(new Class[] {clazz});
    runner.setTestNames(testMethod);
    // runner.setListenerClasses(listeners);
    runner.addListener(new TestNGTextListener());
    if (this.collectCoverage) {
      if (this.offline) {
        runner.addListener(this.initTestClass
            ? (RunListener) Class.forName("com.gzoltar.core.listeners.TestNGListener").newInstance()
            : (RunListener) Class
                .forName("com.gzoltar.core.listeners.TestNGListener", false, classLoader)
                .newInstance());
      } else {
        runner.addListener(new com.gzoltar.core.listeners.TestNGListener());
      }
    }
    runner.setThreadCount(1);
    runner.setPreserveOrder(true);
    runner.setVerbose(0);
    runner.setUseDefaultListeners(false);

    runner.run();
    return null; // FIXME
  }
}
