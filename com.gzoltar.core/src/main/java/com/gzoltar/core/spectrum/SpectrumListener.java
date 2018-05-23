package com.gzoltar.core.spectrum;

import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.ProbeGroup;

public class SpectrumListener implements IEventListener {

  protected Spectrum spectrum;

  public SpectrumListener() {
    this.resetSpectrum();
  }

  public void resetSpectrum() {
    this.spectrum = new Spectrum();
  }

  public ISpectrum getSpectrum() {
    return this.spectrum;
  }

  @Override
  public void regiterProbeGroup(final ProbeGroup probeGroup) {
    this.spectrum.addProbeGroup(probeGroup);
  }

  @Override
  public void endTransaction(final Transaction transaction) {
    this.spectrum.addTransaction(transaction);
  }

  @Override
  public void endSession() {
    // empty
  }
}
