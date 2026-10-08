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
package com.gzoltar.core.listeners;

//importing the new Launcher libraries
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.engine.support.descriptor.MethodSource;

/**
 * JUnit 6 listener.
 */
public final class JUnitListener extends Listener implements TestExecutionListener{

  // called when the TestPlan's execution has started.
  @Override
  public void testPlanExecutionStarted(final TestPlan testPlan) {
    super.onRunStart();
  }

  // called when the TestPlan's execution has finished.
  @Override
  public void testPlanExecutionFinished(final TestPlan testPlan) {
    super.onRunFinish();
  }

  // execution of a test starts.
  @Override
  public void executionStarted(final TestIdentifier testIdentifier) {
    if(testIdentifier.isTest()){
      super.onTestStart();
    }
  }

  // execution of a test finishes.
  @Override
  public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
    if (testIdentifier.isTest()) {
      if (testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED) {
        Throwable exception = testExecutionResult.getThrowable().orElse(new RuntimeException("Test failed"));
        super.onTestFailure(super.traceToString(exception));
      } else if (testExecutionResult.getStatus() == TestExecutionResult.Status.ABORTED) {
        super.onTestSkipped();
      }
      
      // Send the test name converted to GZoltar format
      super.onTestFinish(getGZoltarTestName(testIdentifier));
    }
  }

  //called when a test is skipped/ignored.
  @Override
  public void executionSkipped(final TestIdentifier testIdentifier, final String reason) {
    if(testIdentifier.isTest()){
      super.onTestSkipped();
    }
  }

  // helper method to extract the ClassName#MethodName format.
  private String getGZoltarTestName(TestIdentifier testIdentifier) {
    if (testIdentifier.getSource().isPresent() && testIdentifier.getSource().get() instanceof MethodSource) {
      MethodSource methodSource = (MethodSource) testIdentifier.getSource().get();
      return methodSource.getClassName() + TEST_CLASS_NAME_SEPARATOR + methodSource.getMethodName();
    }
    // Return default name as a fallback if MethodSource is not present
    return testIdentifier.getLegacyReportingName();
  }
}
