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
package com.gzoltar.core.test;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class TestResult {

  private final long runtime;

  private final boolean hasFailed;

  private final String stackTrace;

  protected TestResult(final long runtime, final Throwable failure, final boolean wasSuccessful) {
    this.runtime = runtime;
    this.hasFailed = !wasSuccessful;
    if (this.hasFailed && failure != null) {
      this.stackTrace = this.traceToString(failure);
    } else {
      this.stackTrace = "";
    }
  }

  /**
   * Converts the stack trace of a throwable exception to string.
   * 
   * @param exception The exception thrown.
   * @return A string of the stack trace of a throwable exception.
   */
  private final String traceToString(final Throwable exception) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    exception.printStackTrace(writer);
    return stringWriter.toString();
  }

  /**
   * 
   * @return
   */
  public long getRuntime() {
    return this.runtime;
  }

  /**
   * 
   * @return
   */
  public boolean hasFailed() {
    return this.hasFailed;
  }

  /**
   * 
   * @return
   */
  public String getStackTrace() {
    return this.stackTrace;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "Took " + this.runtime + ". Has it failed? " + this.hasFailed + " " + this.stackTrace;
  }
}
