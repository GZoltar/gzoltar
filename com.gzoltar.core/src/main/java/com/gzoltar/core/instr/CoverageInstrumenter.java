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
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.matchers.ClassNameMatcher;
import com.gzoltar.core.instr.matchers.PrefixMatcher;
import com.gzoltar.core.instr.pass.CoveragePass;
import com.gzoltar.core.instr.pass.IPass;
import javassist.CtClass;

/**
 * Instrument *allowed* classes for coverage purpose
 */
public class CoverageInstrumenter extends AbstractInstrumenter {

  private final Filter filter;

  /**
   * 
   * @param agentConfigs
   */
  public CoverageInstrumenter(final AgentConfigs agentConfigs) {
    super(new IPass[] {
        new CoveragePass(agentConfigs.getInstrumentationLevel())
    });

    // exclude *all* GZoltar's runtime classes from instrumentation
    BlackList excludeGZoltarClasses = new BlackList(new PrefixMatcher("com.gzoltar.internal."));

    // instrument some classes
    WhiteList includeClasses =
        new WhiteList(new ClassNameMatcher(agentConfigs.getIncludes()));

    // do not instrument some classes
    BlackList excludeClasses =
        new BlackList(new ClassNameMatcher(agentConfigs.getExcludes()));

    // do not instrument some classloaders
    BlackList excludeClassLoaders =
        new BlackList(new ClassNameMatcher(agentConfigs.getExclClassloader()));

    this.filter =
        new Filter(excludeGZoltarClasses, includeClasses, excludeClasses, excludeClassLoaders);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome instrument(final CtClass cc, final String ccHash) throws Exception {
    // check whether this class should be instrumented
    if (this.filter.filter(cc) == Outcome.REJECT) {
      return Outcome.REJECT;
    }
    return super.instrument(cc, ccHash);
  }
}
