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
import java.io.PrintWriter;
import java.net.URL;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.jacoco.core.runtime.WildcardMatcher;
import com.gzoltar.core.test.FindTestMethods;
import com.gzoltar.core.test.TestMethod;
import com.gzoltar.maven.utils.ClasspathUtils;

@Mojo(name = "list-test-methods", defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES,
    requiresDependencyResolution = ResolutionScope.TEST,
    requiresDependencyCollection = ResolutionScope.TEST, threadSafe = true)
public class ListTestMethodsMojo extends AbstractMojo {

  /**
   * Maven project.
   */
  @Parameter(property = "project", readonly = true)
  private MavenProject project;

  /**
   * File to which the name of all (JUnit/TestNG) unit test cases in the classpath will be written.
   */
  @Parameter(property = "gzoltar.testFileName",
      defaultValue = "${project.build.directory}/tests.txt")
  private String testFileName;

  /**
   * Expression to identify which test methods to consider, may use wildcard characters (* and ?)
   * and ':' to define more than one expression.
   */
  @Parameter(property = "gzoltar.includes", defaultValue = "*")
  private String includes;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    // make sure classpath has all test dependencies
    try {
      URL[] testClasspathURLs = ClasspathUtils.getTestClasspath(this.project);
      ClasspathUtils.setClassLoaderClasspath(testClasspathURLs);
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }

    try {
      PrintWriter testsWriter = new PrintWriter(this.testFileName, "UTF-8");

      File testClassesDir = new File(this.project.getBuild().getTestOutputDirectory());
      for (TestMethod testMethod : FindTestMethods.findTestMethodsInPath(testClassesDir,
          new WildcardMatcher(this.includes))) {
        testsWriter.println(testMethod.getClassType().name() + "," + testMethod.getLongName());
      }

      testsWriter.close();
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }
}
