package com.gzoltar.fl;

import com.gzoltar.core.spectrum.ISpectrum;

public interface IFormula {

  /**
   * 
   * @param spectrum
   */
  public void diagnose(final ISpectrum spectrum);

  /**
   * Returns the name of the SBL formula
   * 
   * @return
   */
  public String getName();
}
