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

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.gzoltar.core.listeners.Listener;
import com.gzoltar.core.test.TestListener;

public class TestNGTextListener extends TestListener implements ITestListener {

  /**
   * Called before any tests have been run.
   */
  @Override
  public void onStart(ITestContext context) {
    // no-op
  }

  /**
   * Called when all tests have finished.
   */
  @Override
  public void onFinish(ITestContext context) {
    // no-op
  }

  /**
   * Called when an atomic test is about to be started.
   */
  @Override
  public void onTestStart(ITestResult result) {
    // no-op
  }

  /**
   * Called when an atomic test has finished successfully.
   */
  @Override
  public void onTestSuccess(ITestResult result) {
    System.out.println(this.getName(result) + " has finished successfully!");
  }

  /**
   * Called when an atomic test fails.
   */
  @Override
  public void onTestFailure(ITestResult result) {
    System.out.println(traceToString(result.getThrowable()));
    System.out.println(this.getName(result) + " has finished!");
  }

  /**
   * Called when a test is ignored.
   */
  @Override
  public void onTestSkipped(ITestResult result) {
    System.out.println(this.getName(result) + " ignored!");
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    // no-op
  }

  private String getName(ITestResult result) {
    return result.getTestClass().getTestName() + Listener.TEST_CLASS_NAME_SEPARATOR
        + result.getMethod().getMethodName();
  }
}
