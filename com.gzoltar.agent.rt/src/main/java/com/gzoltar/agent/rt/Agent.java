package com.gzoltar.agent.rt;

import com.gzoltar.agent.rt.output.ConsoleOutput;
import com.gzoltar.agent.rt.output.FileOutput;
import com.gzoltar.agent.rt.output.IAgentOutput;
import com.gzoltar.agent.rt.output.NoneOutput;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.AgentOutput;
import com.gzoltar.core.runtime.Collector;
import com.gzoltar.core.spectrum.ISpectrum;

public class Agent implements IAgent {

  private static Agent singleton;

  private final AgentConfigs agentConfigs;

  private IAgentOutput output;

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
    this.agentConfigs = agentConfigs;
    this.output = this.createAgentOutput();
    this.startup();
  }

  private IAgentOutput createAgentOutput() {
    final AgentOutput controllerType = this.agentConfigs.getOutput();
    switch (controllerType) {
      case FILE:
        return new FileOutput(this.agentConfigs);
      case CONSOLE:
        return new ConsoleOutput();
      case NONE:
        return new NoneOutput();
      default:
        throw new AssertionError(controllerType);
    }
  }

  public void startup() {
    Collector.start(this.agentConfigs.getEventListener());
  }

  public void shutdown() {
    try {
      this.output.writeSpectrum(this.getData());
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public ISpectrum getData() {
    return Collector.instance().getBuilder().getSpectrum();
  }
}
