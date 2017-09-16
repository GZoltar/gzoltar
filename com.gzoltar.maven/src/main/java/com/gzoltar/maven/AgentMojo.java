package com.gzoltar.maven;

import java.io.File;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "init")
public class AgentMojo extends AbstractAgentMojo {

  /**
   * Path to the output file for execution data.
   */
  @Parameter(property = "gzoltar.destFile", defaultValue = "${project.build.directory}/gzoltar.exec")
  private File destFile;

  /**
   * @return the destFile
   */
  @Override
  protected File getDestFile() {
    return this.destFile;
  }
}
