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

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.notification.RunListener;
import com.gzoltar.core.test.TestMethod;
import com.gzoltar.core.test.TestTask;

public class JUnitTestTask extends TestTask {

  public JUnitTestTask(final boolean offline, final boolean collectCoverage,
                       final boolean initTestClass, final TestMethod testMethod) {
    super(offline, collectCoverage, initTestClass, testMethod);
  }

  /**
   * Callable method to run JUnit test and return result.
   * 
   * {@inheritDoc}
   */
  @Override
  public JUnitTestResult call() throws Exception {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Class<?> clazz = this.initTestClass ? Class.forName(this.testMethod.getTestClassName())
        : Class.forName(this.testMethod.getTestClassName(), false, classLoader);

    Request request = Request.method(clazz, this.testMethod.getTestMethodName());
    JUnitCore runner = new JUnitCore();
    runner.addListener(new JUnitTextListener());
    if (this.collectCoverage) {
      if (this.offline) {
        runner.addListener(this.initTestClass
            ? (RunListener) Class.forName("com.gzoltar.core.listeners.JUnitListener").newInstance()
            : (RunListener) Class
                .forName("com.gzoltar.core.listeners.JUnitListener", false, classLoader)
                .newInstance());
      } else {
        runner.addListener(new com.gzoltar.core.listeners.JUnitListener());
      }
    }
    JUnitTestResult result = new JUnitTestResult(runner.run(request));
    return result;
  }
}
