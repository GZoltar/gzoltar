package com.gzoltar.agent.rt.output;

import com.gzoltar.core.spectrum.ISpectrum;

public class NoneOutput implements IAgentOutput {

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeSpectrum(final ISpectrum spectrum) throws Exception {
    // nothing to do
  }
}
