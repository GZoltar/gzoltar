package com.gzoltar.agent.rt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.Instrumenter;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.matchers.ClassNameMatcher;
import com.gzoltar.core.instr.matchers.PrefixMatcher;
import com.gzoltar.core.instr.matchers.SourceLocationMatcher;
import com.gzoltar.core.instr.pass.FilterPass;
import com.gzoltar.core.instr.pass.IPass;
import javassist.ClassPool;
import javassist.CtClass;

public class CoverageTransformer implements ClassFileTransformer {

  private final Instrumenter instrumenter;

  private final String buildLocation;

  private final boolean inclNoLocationClasses;

  private final FilterPass filter;

  public CoverageTransformer(final AgentConfigs agentConfigs) throws Exception {
    this.instrumenter = new Instrumenter(agentConfigs.getGranularity());

    this.buildLocation = new File(agentConfigs.getBuildLocation()).toURI().toURL().getPath();
    this.inclNoLocationClasses = agentConfigs.getInclNoLocationClasses();

    // exclude *all* GZoltar's runtime classes from instrumentation
    BlackList excludeGZoltarClasses = new BlackList(new PrefixMatcher("com.gzoltar.internal."));

    // instrument some classes
    WhiteList includeClasses =
        new WhiteList(new ClassNameMatcher(toVMName(agentConfigs.getIncludes())));

    // do not instrument some classes
    BlackList excludeClasses =
        new BlackList(new ClassNameMatcher(toVMName(agentConfigs.getExcludes())));

    // do not instrument some classloaders
    BlackList excludeClassLoaders =
        new BlackList(new ClassNameMatcher(toVMName(agentConfigs.getExclClassloader())));

    this.filter =
        new FilterPass(excludeGZoltarClasses, includeClasses, excludeClasses, excludeClassLoaders);
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
      CtClass cc = cp.makeClass(new ByteArrayInputStream(classfileBuffer));

      // only instrument classes under a build location, e.g., target/classes/ or build/classes/
      SourceLocationMatcher excludeClassesNotInBuildLocation = new SourceLocationMatcher(
          this.inclNoLocationClasses, this.buildLocation, protectionDomain);
      if (!excludeClassesNotInBuildLocation.matches(cc)) {
        return null;
      }

      // check whether this class should be instrumented
      if (this.filter.transform(cc) == IPass.Outcome.CANCEL) {
        return null;
      }

      return this.instrumenter.instrument(cc);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private static String toVMName(final String srcName) {
    return srcName.replace('.', '/');
  }

}
