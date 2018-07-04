/**
 * Copyright (C) 2018 GZoltar contributors.
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
package com.gzoltar.cli.test;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import com.gzoltar.cli.test.junit.FindJUnitAtomicTests;
import com.gzoltar.cli.test.testng.FindTestNGAtomicTests;
import com.gzoltar.cli.utils.ClassType;

public abstract class FindAtomicTests {

  /**
   * 
   * @param testClasses
   * @return
   */
  public static List<String> find(final List<Pair<String, ClassType>> testClasses) {
    List<String> unitTests = new ArrayList<String>();

    for (Pair<String, ClassType> testClass : testClasses) {
      switch (testClass.getRight()) {
        case JUNIT:
          unitTests.addAll(FindJUnitAtomicTests.find(testClass.getLeft()));
          break;
        case TESTNG:
          unitTests.addAll(FindTestNGAtomicTests.find(testClass.getLeft()));
          break;
        default:
          continue;
      }
    }

    return unitTests;
  }
}
