package com.gzoltar.core.test.junit;

//import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import com.gzoltar.core.test.TestResult;

public class JUnitTestResult extends TestResult{
    
    public JUnitTestResult(TestExecutionSummary summary) {
        super(summary.getTimeFinished()-summary.getTimeStarted(),
                summary.getFailures().size() > 0 ?summary.getFailures().get(0).getException():null,
                summary.getTotalFailureCount() == 0);
    }
}
