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

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener.
 */
public final class TestNGListener extends Listener implements ITestListener {

  @Override
  public void onStart(final ITestContext context) {
    super.onRunStart();
  }

  @Override
  public void onFinish(final ITestContext context) {
    super.onRunFinish();
  }

  @Override
  public void onTestStart(final ITestResult result) {
    super.onTestStart();
  }

  @Override
  public void onTestSuccess(final ITestResult result) {
    super.onTestFinish(this.getName(result));
  }

  @Override
  public void onTestFailure(final ITestResult result) {
    super.onTestFailure(this.traceToString(result.getThrowable()));
    super.onTestFinish(this.getName(result));
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(final ITestResult result) {
    super.onTestFailure(this.traceToString(result.getThrowable()));
    super.onTestFinish(this.getName(result));
  }

  @Override
  public void onTestSkipped(final ITestResult result) {
    super.onTestSkipped();
    super.onTestFinish(this.getName(result));
  }

  private String getName(final ITestResult result) {
    return result.getTestClass().getName() + Listener.TEST_CLASS_NAME_SEPARATOR
        + result.getMethod().getMethodName();
  }

}
