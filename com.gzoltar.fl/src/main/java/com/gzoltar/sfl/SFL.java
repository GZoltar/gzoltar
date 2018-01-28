package com.gzoltar.sfl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFaultLocalization;
import com.gzoltar.sfl.formulas.ISFLFormula;

/**
 * Spectrum-based Fault Localization
 * 
 * @author José Campos
 */
public class SFL implements IFaultLocalization {

  private final List<ISFLFormula> formulas = new ArrayList<ISFLFormula>();

  /**
   * 
   * @param sflsNames
   */
  public SFL(final List<String> sflsNames) {
    for (String sflName : sflsNames) {
      ISFLFormula sfl = SFLFormulas.valueOf(sflName.toUpperCase(Locale.ENGLISH)).getFormula();
      this.formulas.add(sfl);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void diagnose(final ISpectrum spectrum) {
    for (ISFLFormula formula : formulas) {
      formula.diagnose(spectrum);
    }
  }
}
