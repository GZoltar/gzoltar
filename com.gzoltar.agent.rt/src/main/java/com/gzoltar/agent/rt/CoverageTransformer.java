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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.Instrumenter;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.filter.DuplicateCollectorReferenceFilter;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.matchers.ClassNameMatcher;
import com.gzoltar.core.instr.matchers.PrefixMatcher;
import com.gzoltar.core.instr.matchers.SourceLocationMatcher;
import javassist.ClassPool;
import javassist.CtClass;

public class CoverageTransformer implements ClassFileTransformer {

  private final Instrumenter instrumenter;

  private final String buildLocation;

  private final boolean inclNoLocationClasses;

  private final Filter filter;

  private final DuplicateCollectorReferenceFilter duplicateCollectorFilter;

  public CoverageTransformer(final AgentConfigs agentConfigs) throws Exception {
    this.instrumenter = new Instrumenter(agentConfigs);

    this.buildLocation = new File(agentConfigs.getBuildLocation()).getCanonicalPath();
    this.inclNoLocationClasses = agentConfigs.getInclNoLocationClasses();

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

    // avoid re-instrumenting previously instrumented classes
    this.duplicateCollectorFilter = new DuplicateCollectorReferenceFilter();
  }

  public byte[] transform(final ClassLoader loader, final String className,
      final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain,
      final byte[] classfileBuffer) {

    if (loader == null) {
      // do not instrument bootstrap classes, e.g., "javax.", "java.", "sun.", "com.sun."
      return null;
    }

    if (classBeingRedefined != null) {
      // avoid re-instrumention
      return null;
    }

    try {
      ClassPool cp = ClassPool.getDefault();
      CtClass cc = cp.makeClassIfNew(new ByteArrayInputStream(classfileBuffer));

      // check whether this class has been instrumented, if so return the
      // previously instrumented code, if not try to instrument it
      if (this.duplicateCollectorFilter.filter(cc) == Outcome.REJECT) {
        return cc.toBytecode();
      }

      // only instrument classes under a build location, e.g., target/classes/ or build/classes/
      SourceLocationMatcher excludeClassesNotInBuildLocation = new SourceLocationMatcher(
          this.inclNoLocationClasses, this.buildLocation, protectionDomain);
      if (!excludeClassesNotInBuildLocation.matches(cc)) {
        return null;
      }

      // check whether this class should be instrumented
      if (this.filter.filter(cc) == Outcome.REJECT) {
        return null;
      }

      return this.instrumenter.instrument(cc);
    } catch (Exception e) {
      System.err.println("GZoltar failed to instrument: " + className);
      e.printStackTrace();
      return null;
    }
  }

}
