package com.gzoltar.report.metrics;

import com.gzoltar.core.spectrum.Spectrum;

public interface Metric {

  public void setSpectrum(Spectrum spectrum);

  public double calculate();

  public String getName();
}
