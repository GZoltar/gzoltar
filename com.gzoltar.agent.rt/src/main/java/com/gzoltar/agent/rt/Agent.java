/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.agent.rt;

import com.gzoltar.agent.rt.output.AgentOutputFactory;
import com.gzoltar.agent.rt.output.OutputEventListener;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.runtime.Collector;
import com.gzoltar.core.spectrum.ISpectrum;

public class Agent implements IAgent {

  private static Agent singleton;

  private final OutputEventListener outputListener;

  public static synchronized Agent getInstance(final AgentConfigs agentConfigs) {
    if (singleton == null) {
      final Agent agent = new Agent(agentConfigs);
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          agent.shutdown();
        }
      });
      singleton = agent;
    }
    return singleton;
  }

  public static synchronized Agent getInstance() {
    if (singleton == null) {
      throw new IllegalStateException("GZoltar agent has not started.");
    }
    return singleton;
  }

  private Agent(final AgentConfigs agentConfigs) {
    this.outputListener =
        new OutputEventListener(AgentOutputFactory.createAgentOutput(agentConfigs));
  }

  public void startup() {
    Collector.instance().addListener(this.outputListener);
  }

  public synchronized void shutdown() {
    singleton = null;
  }

  public ISpectrum getData() {
    return Collector.instance().getSpectrum();
  }
}
