package com.gzoltar.core.listeners;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * JUnit listener.
 */
public class JUnitListener extends Listener {

  @Override
  public void testRunStarted(Description description) {
    super.onRunStart();
  }

  @Override
  public void testRunFinished(Result result) {
    super.onRunFinish();
  }

  @Override
  public void testStarted(Description description) {
    super.onTestStart();
  }

  @Override
  public void testFinished(Description description) {
    super.onTestFinish(this.getName(description));
  }

  @Override
  public void testFailure(Failure failure) {
    super.onTestFailure();
  }

  @Override
  public void testAssumptionFailure(Failure failure) {
    super.onTestFailure();
  }

  @Override
  public void testIgnored(Description description) {
    super.onTestSkipped();
  }

  private String getName(Description description) {
    return description.getClassName() + Listener.TEST_CLASS_NAME_SEPARATOR
        + description.getMethodName();
  }

}
