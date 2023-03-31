package com.gzoltar.core.listeners.junit5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.engine.TestExecutionResult;

import com.gzoltar.core.model.TransactionOutcome;
import com.gzoltar.core.runtime.Collector;

public class Listener implements TestExecutionListener{

    public static final String TEST_CLASS_NAME_SEPARATOR = "#";

    private boolean hasFailed = false;

    private long startTime;

    protected String stackTrace;
    
    /*
     * Called when the execution of the TestPlan has started,
     * before any test has been executed.
     * 
     * Note: This method corresponds to the JUnit 4 method onRunStart
     */
    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        // empty
    }

    /*
     * Called when the execution of the TestPlan has finished, 
     * after all tests have been executed.
     * 
     * Note: This method corresponds to the JUnit 4 method onRunFinish
     */
    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        Collector.instance().endSession();        
    }
    
    /*
     * Called when the execution of a leaf or subtree
     * of the TestPlan has been skipped.
     */
    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        // empty
        
    }

    /* 
     * Called when the execution of a leaf or subtree
     * of the TestPlan is about to be started. 
     * 
     * Note: This method corresponds to the JUnit 4 method onTestStart
     */
    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        this.hasFailed = false;
        this.startTime = System.nanoTime();
        this.stackTrace = "";
        
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
        if (testIdentifier.isTest()){
            if (testExecutionResult.getStatus() == Status.FAILED) {
                this.hasFailed = true;
                //this.stackTrace = trace;
                Optional<Throwable> throwableOp = testExecutionResult.getThrowable();
                if (throwableOp.isPresent()) {
                    Throwable exception= throwableOp.get();
                    this.stackTrace = traceToString(exception);
                }
            }
        }

            Collector.instance().endTransaction(testIdentifier.getDisplayName(),
            this.hasFailed ? TransactionOutcome.FAIL : TransactionOutcome.PASS,
            System.nanoTime() - this.startTime, this.stackTrace);
    }

    /*
     * Called when a new, 
     * dynamic TestIdentifier has been registered.
     */
    @Override
    public void dynamicTestRegistered(TestIdentifier testIdentifier) {
        // what to do?
    
    }

    /*
     * Called when additional test reporting data 
     * has been published for the supplied TestIdentifier.
     */
    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        // what to do?
        
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
