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
package com.gzoltar.sfl;

import java.util.ArrayList;
import java.util.List;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFaultLocalization;
import com.gzoltar.fl.IFormula;

/**
 * Spectrum-based Fault Localization
 * 
 * @author José Campos
 */
public class SFL<F extends IFormula> implements IFaultLocalization<F> {

  private final List<F> formulas = new ArrayList<F>();

  /**
   * 
   * @param sflFormulas
   */
  public SFL(final List<F> sflFormulas) {
    for (F sflFormula : sflFormulas) {
      this.formulas.add(sflFormula);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void diagnose(final ISpectrum spectrum) {
    for (F formula : this.formulas) {
      formula.diagnose(spectrum);
    }
  }
}
