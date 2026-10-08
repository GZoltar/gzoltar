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

import com.gzoltar.core.test.TestMethod;
import com.gzoltar.core.test.TestTask;
// importing the new Launcher libraries
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

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
    if (this.initTestClass) {
      Class.forName(this.testMethod.getTestClassName());
    } else {
      Class.forName(this.testMethod.getTestClassName(), false, classLoader);
    }
    // 1. Create modern Launcher request.
    var request = LauncherDiscoveryRequestBuilder.request()
      .selectors(DiscoverySelectors.selectMethod(this.testMethod.getTestClassName(),
        this.testMethod.getTestMethodName()))
        .build();
    // 2. Create the modern Launcher engine
    Launcher launcher = LauncherFactory.create();
    // 3. Register listeners using the new TestExecutionListener interface
    launcher.registerTestExecutionListeners(new JUnitTextListener());
    
    if (this.collectCoverage) {
      if (this.offline) {
        launcher.registerTestExecutionListeners(this.initTestClass
            ? (TestExecutionListener) Class.forName("com.gzoltar.core.listeners.JUnitListener").newInstance()
            : (TestExecutionListener) Class
                .forName("com.gzoltar.core.listeners.JUnitListener", false, classLoader)
                .newInstance());
      } else {
        launcher.registerTestExecutionListeners(new com.gzoltar.core.listeners.JUnitListener());
      }
    }
    // 4. Register a summary listener to collect test execution results
    SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();
    launcher.registerTestExecutionListeners(summaryListener);

    // 5. Execute the test request asynchronously via the Launcher
    launcher.execute(request);

    // 6. Wrap the JUnit execution summary into GZoltar's custom result format and return it
    JUnitTestResult result = new JUnitTestResult(summaryListener.getSummary());
    return result;
  }
}
