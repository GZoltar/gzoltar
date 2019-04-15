/**
 * Copyright (C) 2019 GZoltar contributors.
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
package com.gzoltar.cli.test.testng;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.testng.TestNG;
import com.gzoltar.cli.test.TestMethod;
import com.gzoltar.cli.test.TestTask;
import com.gzoltar.cli.utils.IsolatingClassLoader;
import com.gzoltar.core.listeners.TestNGListener;

public class TestNGTestTask extends TestTask {

  public TestNGTestTask(final URL[] searchPathURLs, final boolean collectCoverage,
      final TestMethod testMethod) {
    super(searchPathURLs, collectCoverage, testMethod);
  }

  /**
   * Callable method to run JUnit test and return result.
   * 
   * {@inheritDoc}
   */
  @SuppressWarnings("deprecation")
  @Override
  public TestNGTestResult call() throws Exception {
    // Create a new isolated classloader with the same classpath as the current one
    IsolatingClassLoader classLoader = new IsolatingClassLoader(this.searchPathURLs,
        Thread.currentThread().getContextClassLoader());

    // Make the isolated classloader the thread's new classloader. This method is called in a
    // dedicated thread that ends right after this method returns, so there is no need to restore
    // the old/original classloader when it finishes.
    Thread.currentThread().setContextClassLoader(classLoader);

    Class<?> clazz = Class.forName(this.testMethod.getTestClassName(), false, classLoader);

    List<String> testMethod = new ArrayList<String>();
    testMethod.add(this.testMethod.getTestMethodName());

    TestNG runner = new TestNG();
    runner.setTestClasses(new Class[] {clazz});
    runner.setTestNames(testMethod);
    // runner.setListenerClasses(listeners);
    runner.addListener(new TestNGTextListener());
    if (this.collectCoverage) {
      runner.addListener(new TestNGListener());
    }
    runner.setThreadCount(1);
    runner.setPreserveOrder(true);
    runner.setVerbose(0);
    runner.setUseDefaultListeners(false);

    runner.run();
    return null; // FIXME
  }
}
