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
