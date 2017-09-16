package com.gzoltar.maven;

import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Base class for GZoltar Mojos.
 */
public abstract class AbstractGZoltarMojo extends AbstractMojo {

  /**
   * Maven project.
   */
  @Parameter(property = "project", readonly = true)
  private MavenProject project;

  /**
   * A list of class files to include in instrumentation/analysis/reports. May use wildcard
   * characters (* and ?). When not specified everything will be included.
   */
  @Parameter
  private List<String> includes;

  /**
   * A list of class files to exclude from instrumentation/analysis/reports. May use wildcard
   * characters (* and ?). When not specified nothing will be excluded.
   */
  @Parameter
  private List<String> excludes;

  private boolean shouldInstrument() {
    return this.project != null && !"pom".equals(this.project.getPackaging());
  }

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (this.shouldInstrument()) {
      this.executeMojo();
    }
  }

  /**
   * @return Maven project
   */
  protected final MavenProject getProject() {
    return project;
  }

  /**
   * Returns the list of class files to include.
   * 
   * @return class files to include, may contain wildcard characters
   */
  protected List<String> getIncludes() {
    return includes;
  }

  /**
   * Returns the list of class files to exclude.
   * 
   * @return class files to exclude, may contain wildcard characters
   */
  protected List<String> getExcludes() {
    return excludes;
  }

  /**
   * Executes Mojo.
   * 
   * @throws MojoExecutionException if an unexpected problem occurs. Throwing this exception causes
   *         a "BUILD ERROR" message to be displayed.
   * @throws MojoFailureException if an expected problem (such as a compilation failure) occurs.
   *         Throwing this exception causes a "BUILD FAILURE" message to be displayed.
   */
  protected abstract void executeMojo() throws MojoExecutionException, MojoFailureException;
}
