package com.gzoltar.agent.rt.output;

import com.gzoltar.core.spectrum.ISpectrum;

public interface IAgentOutput {

  public void writeSpectrum(final ISpectrum spectrum) throws Exception;
}
