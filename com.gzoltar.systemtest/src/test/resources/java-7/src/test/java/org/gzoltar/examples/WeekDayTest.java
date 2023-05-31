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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class WeekDayTest {

  private WeekDay weekDay;

  @Before
  public void init() {
    this.weekDay = new WeekDay();
  }

  @Test
  public void test1() {
    assertEquals("Monday", this.weekDay.shortToLong("MON"));
  }

  @Test
  public void test2() {
    assertEquals("Tuesday", this.weekDay.shortToLong("TUE"));
  }

  @Test
  public void test3() {
    assertEquals("Wednesday", this.weekDay.shortToLong("WED"));
  }

  @Test
  public void test4() {
    assertEquals("Thursday", this.weekDay.shortToLong("THU"));
  }

  @Test
  public void test5() {
    assertEquals("Friday", this.weekDay.shortToLong("FRI"));
  }

  @Test
  public void test6() {
    assertEquals("Saturday", this.weekDay.shortToLong("SAT"));
  }

  @Test
  public void test7() {
    assertEquals("Sunday", this.weekDay.shortToLong("SUN"));
  }

  @Test
  public void test8() {
    assertEquals("Invalid", this.weekDay.shortToLong("ABC"));
  }

  @Ignore("Not yet fully implemented")
  public void test9() {
    // TBD
  }

  @Test(expected = NullPointerException.class)
  public void test10() {
    this.weekDay.shortToLong(null);
  }

}
