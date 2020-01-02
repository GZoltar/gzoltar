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

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import com.gzoltar.core.listeners.Listener;
import com.gzoltar.core.test.TestListener;

public class JUnitTextListener extends TestListener {

  private boolean hasFailed = false;

  /**
   * {@inheritDoc}
   */
  @Override
  public void testRunStarted(final Description description) {
    // no-op
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void testRunFinished(final Result result) {
    // no-op
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void testStarted(final Description description) {
    this.hasFailed = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void testFinished(final Description description) {
    System.out
        .println(this.getName(description) + " has finished! Has it failed? " + this.hasFailed);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void testFailure(final Failure failure) {
    this.hasFailed = true;
    System.out.println(traceToString(failure.getException()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void testAssumptionFailure(final Failure failure) {
    // no-op
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void testIgnored(final Description description) throws java.lang.Exception {
    System.out.println(this.getName(description) + " ignored!");
  }

  private String getName(final Description description) {
    return description.getClassName() + Listener.TEST_CLASS_NAME_SEPARATOR
        + description.getMethodName();
  }
}
