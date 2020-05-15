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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TestRunner {

  public static TestResult run(final TestTask testTask) {
    FutureTask<TestResult> task = new FutureTask<TestResult>(testTask);
    ThreadGroup group = new ThreadGroup("[thread group for " + testTask.toString() + "]");
    Thread thread = new Thread(group, task, "[thread for " + testTask.toString() + "]");
    thread.start();
    try {
      return task.get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
      killThreadGroup(group);
      thread.interrupt();
      group.destroy();
    }
    return null;
  }

  /**
   * Iterates over all threads in provided group and interrupts each thread.
   */
  private static void killThreadGroup(final ThreadGroup group) {
    Thread[] activeThreads = new Thread[group.activeCount()];
    for (int i = 0; i < group.enumerate(activeThreads); ++i) {
      activeThreads[i].interrupt();
    }
  }
}
