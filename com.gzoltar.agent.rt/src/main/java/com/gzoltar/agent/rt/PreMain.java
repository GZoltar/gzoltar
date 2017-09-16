package com.gzoltar.agent.rt;

import java.lang.instrument.Instrumentation;
import com.gzoltar.core.AgentConfigs;

public final class PreMain {

  public static void premain(final String agentArgs, final Instrumentation inst) {
    final AgentConfigs agentConfigs = new AgentConfigs(agentArgs);
    Agent.getInstance(agentConfigs);
    try {
      inst.addTransformer(new CoverageTransformer(agentConfigs));
    } catch (Exception e) {
      e.printStackTrace();
      // TODO exit?
    }
  }
}
