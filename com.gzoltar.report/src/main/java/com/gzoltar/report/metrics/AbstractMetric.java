package com.gzoltar.report.metrics;

import com.gzoltar.core.spectrum.ISpectrum;

public abstract class AbstractMetric implements Metric {

  protected ISpectrum spectrum;

  @Override
  public void setSpectrum(ISpectrum spectrum) {
    this.spectrum = spectrum;
  }

  protected boolean validMatrix() {
    if (spectrum != null) {
      return spectrum.getComponentsSize() > 0 && spectrum.getTransactionsSize() > 0;
    }
    return false;
  }

}
