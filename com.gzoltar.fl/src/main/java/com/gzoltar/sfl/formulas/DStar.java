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
package com.gzoltar.sfl.formulas;

/**
 * Implementation of DStar coefficient from <i></i>
 * 
 * @author Rui Abreu
 */
public final class DStar extends AbstractSFLFormula {

  @Override
  public String getName() {
    return "DStar";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if ((n10 + n01 == 0) || (n11 == 0)) {
      return 0.0;
    }
    return Math.pow(n11, 2.0) / (n10 + n01);
  }
}
