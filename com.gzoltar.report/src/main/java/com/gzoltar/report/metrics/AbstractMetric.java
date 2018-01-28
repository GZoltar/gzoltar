package com.gzoltar.report.metrics;

import com.gzoltar.core.spectrum.ISpectrum;

public abstract class AbstractMetric implements Metric {

  protected ISpectrum spectrum;

  @Override
  public void setSpectrum(ISpectrum spectrum) {
    this.spectrum = spectrum;
  }

  protected boolean validMatrix() {
    if (this.spectrum != null) {
      return this.spectrum.getNumberOfTargetNodes() > 0 && this.spectrum.getNumberOfTransactions() > 0;
    }
    return false;
  }

}
