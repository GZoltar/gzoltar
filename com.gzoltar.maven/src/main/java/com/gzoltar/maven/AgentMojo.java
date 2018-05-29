package com.gzoltar.maven;

import java.io.File;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "prepare-agent", defaultPhase = LifecyclePhase.INITIALIZE,
    requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true)
public class AgentMojo extends AbstractAgentMojo {

  /**
   * Path to the output file for execution data.
   */
  @Parameter(property = "gzoltar.destFile",
      defaultValue = "${project.build.directory}/gzoltar.ser")
  private File destFile;

  /**
   * @return the destFile
   */
  @Override
  protected File getDestFile() {
    return this.destFile;
  }
}
