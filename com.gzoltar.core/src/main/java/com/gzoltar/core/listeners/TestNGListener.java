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
    super.onTestFailure(result.getThrowable().getStackTrace().toString());
    super.onTestFinish(this.getName(result));
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(final ITestResult result) {
    super.onTestFailure(result.getThrowable().getStackTrace().toString());
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
