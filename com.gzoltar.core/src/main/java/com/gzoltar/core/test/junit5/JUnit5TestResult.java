package com.gzoltar.core.test.junit5;

//import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import com.gzoltar.core.test.TestResult;

public class JUnit5TestResult extends TestResult{
    
    public JUnit5TestResult(TestExecutionSummary summary) {
        super(summary.getTimeFinished()-summary.getTimeStarted(),
                summary.getFailures().get(0).getException(),
                summary.getTotalFailureCount()> 0);
    }
}
