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
import com.gzoltar.core.instr.matchers.ClassNoLineOfCodeMatcher;
import com.gzoltar.core.instr.pass.ClinitPass;
import com.gzoltar.core.instr.pass.IPass;
import com.gzoltar.core.instr.pass.ResetPass;
import javassist.CtClass;

/**
 * Instrument *all* classes for re-clinit purpose
 */
public class ClinitInstrumenter extends AbstractInstrumenter {

  private static final ClassNoLineOfCodeMatcher classNoLineOfCodeMatcher =
      new ClassNoLineOfCodeMatcher();

  private final InstrumentationLevel instrumentationLevel;

  /**
   * 
   * @param agentConfigs
   */
  public ClinitInstrumenter(final AgentConfigs agentConfigs) {
    super(new IPass[] {new ClinitPass(), new ResetPass()});
    this.instrumentationLevel = agentConfigs.getInstrumentationLevel();
  }

  @Override
  public Outcome instrument(ClassLoader loader, CtClass cc, String ccHash) throws Exception {
    if (!classNoLineOfCodeMatcher.matches(cc)) {
      return Outcome.REJECT;
    }
    if (this.instrumentationLevel == InstrumentationLevel.NONE) {
      // minor optimization. if no instrumentation is required, there is no need to waste cpu cycles
      // on it.
      return Outcome.REJECT;
    }
    return super.instrument(loader, cc, ccHash);
  }
}
