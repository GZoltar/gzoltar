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
package com.gzoltar.agent.rt;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.AbstractInstrumenter;
import com.gzoltar.core.instr.ClinitInstrumenter;
import com.gzoltar.core.instr.CoverageInstrumenter;
import com.gzoltar.core.instr.PutGetStaticInstrumenter;
import com.gzoltar.core.instr.matchers.SourceLocationMatcher;
import com.gzoltar.core.util.MD5;
import javassist.ClassPool;
import javassist.CtClass;

public class CoverageTransformer implements ClassFileTransformer {

  private final AbstractInstrumenter[] instrumenters;

  private final String buildLocations;

  private final boolean inclNoLocationClasses;

  public CoverageTransformer(final AgentConfigs agentConfigs) {
    this.instrumenters = new AbstractInstrumenter[] {new PutGetStaticInstrumenter(agentConfigs),
        new ClinitInstrumenter(agentConfigs), new CoverageInstrumenter(agentConfigs),};

    this.buildLocations = agentConfigs.getBuildLocation();
    this.inclNoLocationClasses = agentConfigs.getInclNoLocationClasses();
  }

  @Override
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

      // only instrument classes under a build location, e.g., target/classes/ or build/classes/
      SourceLocationMatcher excludeClassesNotInBuildLocation = new SourceLocationMatcher(
          this.inclNoLocationClasses, this.buildLocations, protectionDomain);
      if (!excludeClassesNotInBuildLocation.matches(cc)) {
        return null;
      }

      // compute bytecode hash before *any* instrumentation takes place
      String hash = MD5.calculateHash(cc);

      for (AbstractInstrumenter instrumenter : this.instrumenters) {
        instrumenter.instrument(cc, hash);
        // for now all instrumenters have the chance to modify the loaded class, however in the
        // future we might want to skip instrumentation if one of the instrumenters rejects it
      }

      return cc.toBytecode();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
