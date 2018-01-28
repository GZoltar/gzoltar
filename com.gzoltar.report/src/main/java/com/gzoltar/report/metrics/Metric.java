package com.gzoltar.report.metrics;

import com.gzoltar.core.spectrum.ISpectrum;

public interface Metric {

  /**
   * 
   * @return
   */
  public double calculate(final ISpectrum spectrum);

  /**
   * 
   * @return
   */
  public String getName();
}
