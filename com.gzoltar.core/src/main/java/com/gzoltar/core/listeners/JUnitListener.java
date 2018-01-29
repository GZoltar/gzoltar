package com.gzoltar.core.listeners;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * JUnit listener.
 */
public final class JUnitListener extends Listener {

  @Override
  public void testRunStarted(final Description description) {
    super.onRunStart();
  }

  @Override
  public void testRunFinished(final Result result) {
    super.onRunFinish();
  }

  @Override
  public void testStarted(final Description description) {
    super.onTestStart();
  }

  @Override
  public void testFinished(final Description description) {
    super.onTestFinish(this.getName(description));
  }

  @Override
  public void testFailure(final Failure failure) {
    super.onTestFailure(failure.getTrace());
  }

  @Override
  public void testAssumptionFailure(final Failure failure) {
    super.onTestFailure(failure.getTrace());
  }

  @Override
  public void testIgnored(final Description description) {
    super.onTestSkipped();
  }

  private String getName(final Description description) {
    return description.getClassName() + Listener.TEST_CLASS_NAME_SEPARATOR
        + description.getMethodName();
  }

}
