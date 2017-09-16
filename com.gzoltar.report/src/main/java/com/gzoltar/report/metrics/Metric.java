package com.gzoltar.report.metrics;

import com.gzoltar.core.spectrum.ISpectrum;

public interface Metric {

  public void setSpectrum(ISpectrum spectrum);

  public double calculate();

  public String getName();
}
