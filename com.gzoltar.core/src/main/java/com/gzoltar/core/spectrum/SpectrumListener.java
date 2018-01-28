package com.gzoltar.core.spectrum;

import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;

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
  public void addNode(final Node node) {
    this.spectrum.addNode(node);
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
