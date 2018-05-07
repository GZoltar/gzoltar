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
      if (this.filter.filter(cc) == Outcome.REJECT) {
        return null;
      }

      return this.instrumenter.instrument(cc);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
