package com.gzoltar.core.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener.
 */
public class TestNGListener extends Listener implements ITestListener {

  @Override
  public void onStart(ITestContext context) {
    super.onRunStart();
  }

  @Override
  public void onFinish(ITestContext context) {
    super.onRunFinish();
  }

  @Override
  public void onTestStart(ITestResult result) {
    super.onTestStart();
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    super.onTestFinish(this.getName(result));
  }

  @Override
  public void onTestFailure(ITestResult result) {
    super.onTestFailure();
    super.onTestFinish(this.getName(result));
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    super.onTestFailure();
    super.onTestFinish(this.getName(result));
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    super.onTestSkipped();
    super.onTestFinish(this.getName(result));
  }

  private String getName(ITestResult result) {
    return result.getTestClass().getName() + Listener.TEST_CLASS_NAME_SEPARATOR
        + result.getMethod().getMethodName();
  }

}
