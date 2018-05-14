package com.gzoltar.core.listeners;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.runner.notification.RunListener;
import com.gzoltar.core.model.TransactionOutcome;
import com.gzoltar.core.runtime.Collector;

/**
 * Test listener.
 */
public class Listener extends RunListener {

  public static final String TEST_CLASS_NAME_SEPARATOR = "#";

  private boolean hasFailed = false;

  private long startTime;

  protected String stackTrace;

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
    this.hasFailed = false;
    this.startTime = System.nanoTime();
    this.stackTrace = "";
  }

  /**
   * Called when an atomic test has finished (whether the test successes or fails).
   * 
   * @param testName
   */
  public final void onTestFinish(final String testName) {
    Collector.instance().endTransaction(testName,
        this.hasFailed ? TransactionOutcome.FAIL : TransactionOutcome.PASS,
        System.nanoTime() - this.startTime, this.stackTrace);
  }

  /**
   * Called when an atomic test fails.
   */
  public final void onTestFailure(String trace) {
    this.hasFailed = true;
    this.stackTrace = trace;
  }

  /**
   * Called when a test will not be run, generally because a test method is annotated with @Ignore
   * or similar annotation.
   */
  public final void onTestSkipped() {
    // empty
  }

  /**
   * Converts the stack trace of a throwable exception to string.
   * 
   * @param exception The exception thrown.
   * @return A string of the stack trace of a throwable exception.
   */
  protected final String traceToString(Throwable exception) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    exception.printStackTrace(writer);
    return stringWriter.toString();
  }
}
