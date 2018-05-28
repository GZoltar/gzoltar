package com.gzoltar.maven;

import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.Instrumenter;

@Mojo(name = "instrument", defaultPhase = LifecyclePhase.PROCESS_CLASSES, threadSafe = true)
public class InstrumentMojo extends AbstractAgentMojo {

  /**
   * {@inheritDoc}
   */
  @Override
  protected File getDestFile() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeMojo() throws MojoExecutionException, MojoFailureException {
    final File projectClassesDir = new File(getProject().getBuild().getOutputDirectory());
    if (!projectClassesDir.exists()) {
      getLog()
          .info("Skipping GZoltar execution due to missing classes directory:" + projectClassesDir);
      return;
    }

    final File instrumentedClassesDir =
        new File(getProject().getBuild().getDirectory(),
            "instrumented-classes" + File.separator + "gzoltar");
    instrumentedClassesDir.mkdirs();

    // configure instrumentation
    AgentConfigs agentConfigs = this.createAgentConfigurations();
    agentConfigs.setInstrumentationLevel(InstrumentationLevel.OFFLINE);
    Instrumenter instrumenter = new Instrumenter(agentConfigs);

    // instrument recursively
    try {
      instrumenter.instrumentRecursively(projectClassesDir, instrumentedClassesDir);
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

}
