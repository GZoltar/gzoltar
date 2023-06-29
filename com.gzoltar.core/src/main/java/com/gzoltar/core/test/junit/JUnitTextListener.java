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
package com.gzoltar.core.test.junit;

import com.gzoltar.core.listeners.Listener;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.util.Optional;

public class JUnitTextListener extends SummaryGeneratingListener {

    private boolean hasFailed = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void testPlanExecutionStarted(final TestPlan testPlan) {
        super.testPlanExecutionStarted(testPlan);
        System.out.println(testPlan);
        System.out.println("Test plan execution has started!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testPlanExecutionFinished(final TestPlan testPlan) {
        super.testPlanExecutionFinished(testPlan);
        System.out.println("Test plan execution has finished!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executionSkipped(final TestIdentifier testIdentifier, final String reason) {
        if (testIdentifier.isTest()) {
            super.executionSkipped(testIdentifier, reason);
            System.out.println(this.getName(testIdentifier) + " has been skipped! Reason: " + reason);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executionStarted(final TestIdentifier testIdentifier) {
        if (testIdentifier.isTest()) {
            super.executionStarted(testIdentifier);
            this.hasFailed = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.isTest()) {
            super.executionFinished(testIdentifier, testExecutionResult);
            this.hasFailed = testExecutionResult.getStatus().equals(TestExecutionResult.Status.FAILED);
            System.out.println(this.getName(testIdentifier) + " has finished! Has it failed? " + this.hasFailed);
        }
    }

    private String getName(final TestIdentifier testIdentifier) {
        if (testIdentifier.isTest()) {
            Optional<TestSource> testSource = testIdentifier.getSource();

            if (testSource.isPresent()) {
                MethodSource classSource = (MethodSource) testSource.get();
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
