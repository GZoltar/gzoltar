package com.gzoltar.agent.rt.output;

import com.gzoltar.core.spectrum.ISpectrum;

public interface IAgentOutput {

  /**
   * Write a spectrum instance to a location determined by the agent controller.
   * 
   * @param spectrum a {@link com.gzoltar.core.spectrum.ISpectrum} object
   * @throws Exception in case writing fails
   */
  public void writeSpectrum(final ISpectrum spectrum) throws Exception;
}
