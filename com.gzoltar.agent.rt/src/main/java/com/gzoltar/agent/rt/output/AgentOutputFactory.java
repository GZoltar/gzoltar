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
package com.gzoltar.agent.rt.output;

import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.AgentOutput;

public final class AgentOutputFactory {

  /**
   * Create an instance of {@link com.gzoltar.agent.rt.output.IAgentOutput} based on the agent
   * configuration.
   * 
   * @param agentConfigs an object of {@link com.gzoltar.core.AgentConfigs}
   * @return an object of {@link com.gzoltar.agent.rt.output.IAgentOutput}
   */
  public static IAgentOutput createAgentOutput(final AgentConfigs agentConfigs) {
    final AgentOutput controllerType = agentConfigs.getOutput();
    switch (controllerType) {
      case FILE:
        return new FileOutput(agentConfigs);
      case CONSOLE:
        return new ConsoleOutput();
      case NONE:
        return new NoneOutput();
      default:
        throw new AssertionError(controllerType);
    }
  }
}
