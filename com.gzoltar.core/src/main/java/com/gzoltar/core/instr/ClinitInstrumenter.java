/**
 * Copyright (C) 2018 GZoltar contributors.
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
package com.gzoltar.core.instr;

import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.pass.ClinitPass;
import com.gzoltar.core.instr.pass.IPass;
import com.gzoltar.core.instr.pass.ResetPass;

public class ClinitInstrumenter extends AbstractInstrumenter {

  /**
   * 
   * @param agentConfigs
   */
  public ClinitInstrumenter(final AgentConfigs agentConfigs) {
    super(new IPass[] {
        new ClinitPass(agentConfigs.getInstrumentationLevel()),
        new ResetPass(agentConfigs.getInstrumentationLevel())
    });
  }
}
