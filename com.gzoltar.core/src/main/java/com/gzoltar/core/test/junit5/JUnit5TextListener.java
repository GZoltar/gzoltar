package com.gzoltar.core.test.junit5;

import java.util.Optional;

import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
//import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.engine.support.descriptor.ClassSource;

import com.gzoltar.core.listeners.junit5.Listener;

public class JUnit5TextListener extends SummaryGeneratingListener {

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
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
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
        if (testIdentifier.isTest()) {
            Optional<TestSource> testSource = testIdentifier.getSource();
            if (testSource.isPresent()) {
                ClassSource classSource = (ClassSource) testSource.get();
                String sourceName = classSource.getClassName();
                return sourceName + Listener.TEST_CLASS_NAME_SEPARATOR + testIdentifier.getDisplayName();
            } else {
                return "<unknown source>"
                    + Listener.TEST_CLASS_NAME_SEPARATOR + testIdentifier.getDisplayName();
            }
        } else {
            return testIdentifier.getDisplayName();
        }
    }



}
