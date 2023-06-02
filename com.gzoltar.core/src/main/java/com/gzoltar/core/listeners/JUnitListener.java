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
package com.gzoltar.core.listeners;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * JUnit listener.
 */
public final class JUnitListener extends Listener {

  @Override
  public void testRunStarted(final Description description) {
    super.onRunStart();
  }

  @Override
  public void testRunFinished(final Result result) {
    super.onRunFinish();
  }

  @Override
  public void testStarted(final Description description) {
    super.onTestStart();
  }

  @Override
  public void testFinished(final Description description) {
    super.onTestFinish(this.getName(description));
  }

  @Override
  public void testFailure(final Failure failure) {
    super.onTestFailure(failure.getTrace());
  }

  @Override
  public void testAssumptionFailure(final Failure failure) {
    // an assumption failure is not propagated to org.junit.runner.Result
  }

  @Override
  public void testIgnored(final Description description) {
    super.onTestSkipped();
  }

  private String getName(final Description description) {
    return description.getClassName() + Listener.TEST_CLASS_NAME_SEPARATOR
        + description.getMethodName();
  }

}
