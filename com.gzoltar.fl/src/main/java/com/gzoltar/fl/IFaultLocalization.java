package com.gzoltar.fl;

import com.gzoltar.core.spectrum.ISpectrum;

public interface IFaultLocalization<F extends IFormula> {

  /**
   * 
   * @param spectrum a {@link com.gzoltar.core.spectrum.ISpectrum} object
   */
  public void diagnose(final ISpectrum spectrum);
}
