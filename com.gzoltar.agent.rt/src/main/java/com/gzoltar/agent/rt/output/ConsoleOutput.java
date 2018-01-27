package com.gzoltar.agent.rt.output;

import com.gzoltar.core.spectrum.ISpectrum;

public class ConsoleOutput implements IAgentOutput {

  @Override
  public void writeSpectrum(final ISpectrum spectrum) throws Exception {
    System.out.println(spectrum.toString());
  }
}
