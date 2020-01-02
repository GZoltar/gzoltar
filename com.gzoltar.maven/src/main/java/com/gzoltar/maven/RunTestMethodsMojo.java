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
package com.gzoltar.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import com.gzoltar.core.util.SystemProperties;
import com.gzoltar.maven.utils.ClasspathUtils;
import com.gzoltar.maven.utils.Launcher;

@Mojo(name = "run-test-methods", defaultPhase = LifecyclePhase.TEST,
    requiresDependencyResolution = ResolutionScope.TEST,
    requiresDependencyCollection = ResolutionScope.TEST, threadSafe = true)
@Execute(goal = "run-test-methods")
public class RunTestMethodsMojo extends AgentMojo {

  /**
   * File to which the name of all (JUnit/TestNG) unit test cases in the classpath will be read.
   */
  @Parameter(property = "gzoltar.testFileName",
      defaultValue = "${project.build.directory}/tests.txt")
  private String testFileName;

  @Parameter(property = "gzoltar.offline", defaultValue = "false")
  private boolean offline;

  @Parameter(property = "gzoltar.collectCoverage", defaultValue = "true")
  private boolean collectCoverage;

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeMojo() throws MojoExecutionException, MojoFailureException {
    final File testMethodsFile = new File(this.testFileName);
    if (!testMethodsFile.exists() || !testMethodsFile.canRead()) {
      throw new MojoExecutionException(testMethodsFile + " does not exist or cannot be read");
    }

    try {
      List<String> commandLineArgs = new ArrayList<String>();

      if (!this.offline) {
        commandLineArgs.add(this.prepareAgentVM());
      } else {
        commandLineArgs.add("-Dgzoltar-agent.destfile=" + this.getDestFile().getAbsolutePath());
        commandLineArgs.add("-Dgzoltar-agent.output=" + this.getOutput());
      }

      commandLineArgs.add("-classpath");
      commandLineArgs.add(StringUtils.join(ClasspathUtils.getTestClasspath(this.getProject()),
          SystemProperties.PATH_SEPARATOR));

      commandLineArgs.add("com.gzoltar.cli.Main");

      commandLineArgs.add("runTestMethods");
      commandLineArgs.add("--testMethods");
      commandLineArgs.add(this.testFileName);
      if (this.offline) {
        commandLineArgs.add("--offline");
      }
      if (this.collectCoverage) {
        commandLineArgs.add("--collectCoverage");
      }

      if (Launcher.launch(commandLineArgs) != 0) {
        throw new MojoFailureException("Execution of each test case in isolation has failed!");
      }
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }
}
