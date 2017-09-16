package com.gzoltar.agent.rt;

import com.gzoltar.core.spectrum.ISpectrum;

public interface IAgent {

  public void startup();

  public void shutdown();

  public ISpectrum getData();
}
