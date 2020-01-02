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
package com.gzoltar.core.instr;

public enum InstrumentationLevel {

  /**
   * A FULL instrumentation level instruments a class (i.e., adds probes to a particular class) and
   * generates all nodes (i.e., information about probes).
   */
  FULL,

  /**
   * A NONE instrumentation level only generates nodes (i.e., information about probes). No
   * bytecode is ever injected.
   */
  NONE,

  /**
   * A OFFLINE instrumentation level only instruments a class (i.e., adds probes to a particular
   * class).
   */
  OFFLINE

}
