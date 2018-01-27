package com.gzoltar.core.spectrum;

import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.model.NodeType;

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
  public void endTransaction(final String transactionName, final boolean[] activity,
      final boolean isError) {
    this.spectrum.addTransaction(transactionName, activity, isError);
  }

  @Override
  public void endTransaction(final String transactionName, final boolean[] activity,
      final int hashCode, final boolean isError) {
    this.spectrum.addTransaction(transactionName, activity, hashCode, isError);
  }

  @Override
  public void addNode(final int id, final String name, final NodeType type, final int parentId) {
    this.spectrum.getTree().addNode(name, type, parentId);
  }

  @Override
  public void addProbe(final int id, final int nodeId) {
    this.spectrum.addProbe(id, nodeId);
  }

  @Override
  public void endSession() {
    // empty
  }

}
