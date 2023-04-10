package com.gzoltar.core.test.junit5;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;

import com.gzoltar.core.listeners.junit5.Listener;

public class JUnit5TextListener extends TestExecutionListener {

    private boolean hasFailed = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void testPlanExecutionStarted(final TestPlan testPlan) {
        System.out.println("Test plan execution has started!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testPlanExecutionFinished(final TestPlan testPlan) {
        System.out.println("Test plan execution has finished!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executionSkipped(final TestIdentifier testIdentifier, final String reason) {
        System.out.println(this.getName(testIdentifier) + " has been skipped! Reason: " + reason);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executionStarted(final TestIdentifier testIdentifier) {
        this.hasFailed = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executionFinished(final TestIdentifier testIdentifier, final TestExecutionResult) {
        this.hasFailed = testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED;
        System.out.println(this.getName(testIdentifier) + " has finished! Has it failed? " + this.hasFailed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportingEntryPublished(final TestIdentifier testIdentifier, final ReportEntry entry) {
        // no-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dynamicTestRegistered(final TestIdentifier testIdentifier) {
        // no-op
    }

    private String getName(final TestIdentifier testIdentifier) {
        // will this work?
        if (testIdentifier.isTest()) {
            return testIdentifier.getSource().get().getClassName() + Listener.TEST_CLASS_NAME_SEPARATOR
                    + testIdentifier.getDisplayName();
        } else if (testIdentifier.isContainer()) {
            return testIdentifier.getDisplayName();
        }
    }



}
