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

import com.gzoltar.core.model.TransactionOutcome;
import com.gzoltar.core.runtime.Collector;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * JUnit listener.
 */
public final class JUnitListener extends Listener {
  /*
   * Called when the execution of the TestPlan has finished,
   * after all tests have been executed.
   *
   * Note: This method corresponds to the JUnit 4 method onRunFinish
   */
  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    super.testPlanExecutionFinished(testPlan);
    onRunFinish();
  }


  /*
   * Called when the execution of a leaf or subtree
   * of the TestPlan is about to be started.
   *
   * Note: This method corresponds to the JUnit 4 method onTestStart
   */
  @Override
  public void executionStarted(TestIdentifier testIdentifier) {
    super.executionStarted(testIdentifier);
    onTestStart();
  }

  /**
   * Called when the execution of a leaf or subtree
   * of the TestPlan has finished, regardless of the outcome.
   * @param testIdentifier: the TestIdentifier of the test or container that was executed
   * @param testExecutionResult: the result of the test execution
   *
   * Note: This method corresponds to the JUnit 4 method onTestFinished and onTestFailure
   */
  @Override
  public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
    super.executionFinished(testIdentifier,testExecutionResult);
    System.out.println("test:"+testIdentifier.getDisplayName());
    if (testIdentifier.isTest()){
      if (testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED) {
        Optional<Throwable> throwableOp = testExecutionResult.getThrowable();
        throwableOp.ifPresent(throwable -> onTestFailure(traceToString(throwable)));
      }
      onTestFinish(testIdentifier.getDisplayName());
    }


  }

}
