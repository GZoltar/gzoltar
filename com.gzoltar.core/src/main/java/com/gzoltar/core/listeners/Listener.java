package com.gzoltar.core.listeners;

import org.junit.runner.notification.RunListener;
import com.gzoltar.core.runtime.Collector;

/**
 * Test listener.
 */
public class Listener extends RunListener {

  public static final String TEST_CLASS_NAME_SEPARATOR = "#";

  private boolean isError = false;

  /**
   * Called before any tests have been run.
   */
  public final void onRunStart() {
    // empty
  }

  /**
   * Called when all tests have finished.
   */
  public final void onRunFinish() {
    Collector.instance().endSession();
  }

  /**
   * Called when an atomic test is about to be started.
   */
  public final void onTestStart() {
    this.isError = false;
    Collector.instance().startTransaction();
  }

  /**
   * Called when an atomic test has finished (whether the test successes or fails).
   * 
   * @param testName
   */
  public final void onTestFinish(final String testName) {
    Collector.instance().endTransaction(testName, this.isError);
  }

  /**
   * Called when an atomic test fails.
   */
  public final void onTestFailure() {
    this.isError = true;
  }

  /**
   * Called when a test will not be run, generally because a test method is annotated with @Ignore
   * or similar annotation.
   */
  public final void onTestSkipped() {
    // empty
  }

}
