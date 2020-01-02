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
package com.gzoltar.ant.listener;

import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitResultFormatter;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import com.gzoltar.core.listeners.Listener;
import junit.framework.AssertionFailedError;
import junit.framework.Test;

/**
 * JUnit listener.
 */
public class JUnitFormatter extends Listener implements JUnitResultFormatter {

  /**
   * 
   */
  public JUnitFormatter() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startTest(Test test) {
    super.onTestStart();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endTest(Test test) {
    super.onTestFinish(this.getName(test));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startTestSuite(JUnitTest suite) throws BuildException {
    // no-op
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endTestSuite(JUnitTest suite) throws BuildException {
    // no-op
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addFailure(Test test, AssertionFailedError assertionFailedError) {
    super.onTestFailure(this.traceToString(assertionFailedError));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addError(Test test, Throwable throwable) {
    super.onTestFailure(this.traceToString(throwable));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOutput(OutputStream out) {
    // nop
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSystemError(String err) {
    // no-op
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSystemOutput(String out) {
    // no-op
  }

  private String getName(Test test) {
    String className = null;
    String methodName = null;

    {
      Pattern regexp = Pattern.compile("(.*)\\((.*)\\)");
      Matcher match = regexp.matcher(test.toString());
      if (match.matches()) {
        className = match.group(2);
        methodName = match.group(1);
      }
    }
    {
      // for some weird reason this format is used for Timeout in Junit4
      Pattern regexp = Pattern.compile("(.*):(.*)");
      Matcher match = regexp.matcher(test.toString());
      if (match.matches()) {
        className = match.group(1);
        methodName = match.group(2);
      }
    }

    return className + Listener.TEST_CLASS_NAME_SEPARATOR + methodName;
  }

}
