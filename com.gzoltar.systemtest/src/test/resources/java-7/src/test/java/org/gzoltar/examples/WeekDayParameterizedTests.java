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

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WeekDayParameterizedTests {

  @Parameters(name = "{index}: shortToLong({0})={1}")
  public static Iterable<Object[]> data() {
    return Arrays.asList(new Object[][] {
      { "MON", "Monday" },
      { "TUE", "Tuesday" },
      { "WED", "Wednesday" },
      { "THU", "Thursday" },
      { "FRI", "Friday" },
      { "SAT", "Saturday" },
      { "SUN", "Sunday" },
      { "ABC", "Invalid" },
    });
  }

  private String input;

  private String expected;

  public WeekDayParameterizedTests(final String input, final String expected) {
    this.input = input;
    this.expected = expected;
  }

  @Test
  public void test() {
    WeekDay weekDay = new WeekDay();
    assertEquals(this.expected, weekDay.shortToLong(this.input));
  }

}
