package com.gzoltar.agent.rt;

import java.lang.instrument.Instrumentation;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.SystemClassInstrumenter;

public final class PreMain {

  private PreMain() {
    // no instance
  }

  /**
   * This method is called by the JVM to initialize Java agents.
   * 
   * @param agentArgs agent arguments
   * @param inst instrumentation callback provided by the JVM
   * @throws Exception in case initialization fails
   */
  public static void premain(final String agentArgs, final Instrumentation inst) throws Exception {
    final AgentConfigs agentConfigs = new AgentConfigs(agentArgs);
    IAgent agent = Agent.getInstance(agentConfigs);
    agent.startup();

    // Instruments a pre-defined system class, i.e., adds a static field to a system class so that
    // other classes could access GZoltar's runtime collector
    if (agentConfigs.getInstrumentationLevel().equals(InstrumentationLevel.FULL)) {
      SystemClassInstrumenter.instrumentSystemClass(inst,
          InstrumentationConstants.SYSTEM_CLASS_NAME,
          InstrumentationConstants.SYSTEM_CLASS_FIELD_NAME);
    }

    inst.addTransformer(new CoverageTransformer(agentConfigs));
  }
}
