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
