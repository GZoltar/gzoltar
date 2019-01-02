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
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.PutGetStaticInstrumenter;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.matchers.PrefixMatcher;
import com.gzoltar.core.util.MD5;
import javassist.ClassPool;
import javassist.CtClass;

public class CoverageTransformer implements ClassFileTransformer {

  private final AbstractInstrumenter[] instrumenters;

  private final Filter blackListClasses;

  public CoverageTransformer(final AgentConfigs agentConfigs) {
    this.instrumenters = new AbstractInstrumenter[] {new CoverageInstrumenter(agentConfigs),
        new PutGetStaticInstrumenter(agentConfigs), new ClinitInstrumenter(agentConfigs)};

    this.blackListClasses = new Filter(
        new BlackList(new PrefixMatcher("com.gzoltar.", "javax.", "java.", "sun.", "com.sun.",
            "org.junit.", "junit.framework.", "org.hamcrest.", "org.apache.tools.ant.")));
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
      CtClass cc = cp.makeClass(new ByteArrayInputStream(classfileBuffer));

      if (this.blackListClasses.filter(cc) == Outcome.REJECT) {
        cc.detach();
        return null;
      }

      // compute bytecode hash before *any* instrumentation takes place
      String hash = MD5.calculateHash(cc);

      for (AbstractInstrumenter instrumenter : this.instrumenters) {
        instrumenter.instrument(loader, cc, hash);
        // for now all instrumenters have the chance to modify the loaded class, however in the
        // future we might want to skip instrumentation if one of the instrumenters rejects it
      }

      byte[] b = cc.toBytecode();
      cc.detach();

      return b;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
