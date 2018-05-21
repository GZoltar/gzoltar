package com.gzoltar.agent.rt;

import java.lang.instrument.Instrumentation;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.SystemClassInstrumenter;

public final class PreMain {

  public static void premain(final String agentArgs, final Instrumentation inst) {
    final AgentConfigs agentConfigs = new AgentConfigs(agentArgs);
    Agent.getInstance(agentConfigs);

    try {
      // Instruments a pre-defined system class loader, i.e., adds a static field to a system class
      // loader so that other classes could access GZoltar's runtime collector
      if (!agentConfigs.getOfflineInstrumentation()) {
        SystemClassInstrumenter.instrumentSystemClass(inst,
            InstrumentationConstants.SYSTEM_CLASS_NAME_JVM,
            InstrumentationConstants.SYSTEM_CLASS_FIELD_NAME);
      }

      inst.addTransformer(new CoverageTransformer(agentConfigs));
    } catch (Exception e) {
      e.printStackTrace();
      // TODO exit?
    }
  }
}
