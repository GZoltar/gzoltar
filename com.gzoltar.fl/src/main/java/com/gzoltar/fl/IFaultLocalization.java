package com.gzoltar.fl;

import com.gzoltar.core.spectrum.ISpectrum;

public interface IFaultLocalization {

  /**
   * 
   * @param spectrum a {@link com.gzoltar.core.spectrum.ISpectrum} object
   */
  public void diagnose(final ISpectrum spectrum);
}
