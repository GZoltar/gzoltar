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
package org.gzoltar.examples;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StaticFieldTest {

  @Test
  public void test1() {
    assertEquals("foo", StaticField.getFoo());
  }

  @Test
  public void test2() {
    // 0 lines covered
  }

  /**
   * This test case should have the exact same coverage as test1. To report
   * accurate code coverage of static fields, GZoltar runs each test case in
   * isolation. Otherwise, only the first test case at loading and executing the
   * static constructor of the class under would have coverage of static fields.
   */
  @Test
  public void test3() {
    assertEquals("abc", StaticField.getFoo());
  }
}
