package com.gzoltar.report.metrics;

import com.gzoltar.core.spectrum.ISpectrum;

public interface Metric {

  /**
   * 
   * @param spectrum
   */
  public void setSpectrum(ISpectrum spectrum);

  /**
   * 
   * @return
   */
  public double calculate();

  /**
   * 
   * @return
   */
  public String getName();
}
