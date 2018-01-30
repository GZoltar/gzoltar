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
