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
 * Implementation of Sorensen-Dice coefficient from <i>Measures of the Amount of Ecologic
 * Association Between Species</i> and <i>A Method of Establishing Groups of Equal Amplitude in
 * Plant Sociology Based on Similarity of Species Content and Its Application to Analyses of the
 * Vegetation on Danish Commons</i>
 * 
 * @author José Campos
 */
public final class SorensenDice extends AbstractSFLFormula {

  @Override
  public String getName() {
    return "SorensenDice";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (2 * n11 + n01 + n10 == 0) {
      return 0.0;
    }
    return (2 * n11) / (2 * n11 + n01 + n10);
  }
}
