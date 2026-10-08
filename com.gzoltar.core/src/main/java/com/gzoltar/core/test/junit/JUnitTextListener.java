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
import com.gzoltar.core.test.TestListener;
//importing the new Launcher libraries.
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

public class JUnitTextListener extends TestListener implements TestExecutionListener {

  /**
   * {@inheritDoc}
   */
  //called when the TestPlan's execution has started.
  @Override
  public void testPlanExecutionStarted(final TestPlan testPlan) {
    // no-op
  }

  /**
   * {@inheritDoc}
   */
  //called when the TestPlan's execution has finished.
  @Override
  public void testPlanExecutionFinished(final TestPlan testPlan) {
    // no-op
  }

  /**
   * {@inheritDoc}
   */
  // execution of a test finishes.
  @Override
  public void executionFinished(final TestIdentifier testIdentifier,final TestExecutionResult testExecutionResult) {
    //check if it is the actual test.
    if(testIdentifier.isTest()){
      //check if it has failed.
      boolean hasFailed = testExecutionResult.getStatus()==TestExecutionResult.Status.FAILED;
      //if failed,notify that.
      if(hasFailed){
        testExecutionResult.getThrowable().ifPresent(t ->
          System.out.println(traceToString(t)));
      }
      System.out.println(this.getName(testIdentifier) + " has finished! Has it failed? " + hasFailed);
    }
  }

  /**
   * {@inheritDoc}
   */
  // called when a test is skipped/ignored
  @Override
  public void executionSkipped(final TestIdentifier testIdentifier,final String reason){
    if(testIdentifier.isTest()){
      System.out.println(this.getName(testIdentifier) + " ignored!");
    }   
  }

  //helper method to extract the ClassName#MethodName format.
  private String getName(final TestIdentifier testIdentifier) {
    if(testIdentifier.getSource().isPresent() && testIdentifier.getSource().get() instanceof MethodSource){
      MethodSource methodSource =(MethodSource) testIdentifier.getSource().get();
      return methodSource.getClassName() + Listener.TEST_CLASS_NAME_SEPARATOR
        + methodSource.getMethodName();
    }
    return testIdentifier.getLegacyReportingName();
  }
}
