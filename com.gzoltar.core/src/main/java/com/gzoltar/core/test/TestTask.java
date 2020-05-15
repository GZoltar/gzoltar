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

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TestTask implements Callable<TestResult> {

  private static final AtomicInteger INC = new AtomicInteger();

  protected final int id;

  protected final URL[] searchPathURLs;

  protected final boolean offline;

  protected final boolean collectCoverage;

  protected final boolean initTestClass;

  protected final TestMethod testMethod;

  /**
   * Constructor for task to run a JUnit test method.
   */
  protected TestTask(final URL[] searchPathURLs, final boolean offline,
      final boolean collectCoverage, final boolean initTestClass, final TestMethod testMethod) {
    this.id = INC.incrementAndGet();
    this.searchPathURLs = searchPathURLs;
    this.offline = offline;
    this.collectCoverage = collectCoverage;
    this.initTestClass = initTestClass;
    this.testMethod = testMethod;
  }

  /**
   * Returns the JUnit test method.
   * 
   * @return
   */
  public TestMethod getTestMethod() {
    return this.testMethod;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "[" + this.id + "] :: " + this.testMethod.getLongName();
  }
}
