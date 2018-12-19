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
import com.gzoltar.core.instr.pass.CoveragePass;
import com.gzoltar.core.instr.pass.IPass;

/**
 * Several APIs to instrument Java class definitions for coverage tracing.
 */
public class CoverageInstrumenter extends AbstractInstrumenter {

  /**
   * 
   * @param agentConfigs
   */
  public CoverageInstrumenter(final AgentConfigs agentConfigs) {
    super(new IPass[] {
        new CoveragePass(agentConfigs.getInstrumentationLevel())
    });
  }
}
