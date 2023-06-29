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

/**
 * Java-7 example
 */
public class WeekDay {

  private int dummy;

  public WeekDay() {
    this.dummy = 0;
  }

  public String shortToLong(String day) {
    if (day == null) {
      throw new NullPointerException("Argument day cannot be null!");
    }
    // Before JDK 7, only integral types (such as int, char) can be used as
    // selector for switch-case statement.  In JDK 7, you can use a String
    // object as the selector. For example:
    return switch (day) {   // switch on String selector
      case "MON" -> "Monday";
      case "TUE" -> "Tuesday";
      case "WED" -> "Wednesday";
      case "THU" -> "Thurday"; /* FAULT, Thurday -> Thursday */
      case "FRI" -> "Friday";
      case "SAT" -> "Saturday";
      case "SUN" -> "Sunday";
      default ->  "Invalid";
    };
  }

  public int getDummy() {
    return this.dummy;
  }

}
