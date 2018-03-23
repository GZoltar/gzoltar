package com.gzoltar.maven;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;
import com.gzoltar.core.AgentConfigs;

/**
 * Base class for preparing a property pointing to the GZoltar runtime agent that can be passed as a
 * VM argument to the application under test.
 */
public abstract class AbstractAgentMojo extends AbstractGZoltarMojo {

  /**
   * Name of the GZoltar Agent artifact.
   */
  private static final String AGENT_ARTIFACT_NAME = "com.gzoltar:com.gzoltar.agent";

  /**
   * Name of the property used in maven-surefire-plugin.
   */
  private static final String SUREFIRE_ARG_LINE = "argLine";

  /**
   * Map of plugin artifacts.
   */
  @Parameter(property = "plugin.artifactMap", required = true, readonly = true)
  private Map<String, Artifact> pluginArtifactMap;

  /**
   * A list of class loader names, that should be excluded from execution analysis. The list entries
   * are separated by a colon (:) and may use wildcard characters (* and ?). This option might be
   * required in case of special frameworks that conflict with GZoltar code instrumentation, in
   * particular class loaders that do not have access to the Java runtime classes.
   */
  @Parameter(property = "gzoltar.exclClassLoaders", defaultValue = "sun.reflect.DelegatingClassLoader")
  private String exclClassLoaders;

  /**
   * Specifies whether classes without source location should be instrumented.
   */
  @Parameter(property = "gzoltar.inclNoLocationClasses", defaultValue = "false")
  private Boolean inclNoLocationClasses;

  /**
   * Output method to use for writing coverage data. Valid options are:
   * <ul>
   * <li>file: At VM termination execution data is written to a file.</li>
   * <li>console: At VM termination execution data is written to the stdout (default).</li>
   * <li>none: Do not produce any output.</li>
   * </ul>
   */
  @Parameter(property = "gzoltar.output", defaultValue = "CONSOLE")
  private String output;

  /**
   * Specifies the granularity level. Valid options are:
   * <ul>
   * <li>line (default)</li>
   * <li>method</li>
   * <li>basicblock</li>
   * </ul>
   */
  @Parameter(property = "gzoltar.granularity", defaultValue = "LINE")
  private String granularity;

  /**
   * Specifies whether public methods of each class under test should be instrumented. Default is
   * <code>true</code>.
   */
  @Parameter(property = "gzoltar.inclPublicMethods", defaultValue = "true")
  private Boolean inclPublicMethods;

  /**
   * Specifies whether public static constructors of each class under test should be instrumented.
   * Default is <code>false</code>.
   */
  @Parameter(property = "gzoltar.inclStaticConstructors", defaultValue = "false")
  private Boolean inclStaticConstructors;

  /**
   * Specifies whether methods annotated with @deprecated of each class under test should be
   * instrumented. Default is <code>true</code>.
   */
  @Parameter(property = "gzoltar.inclDeprecatedMethods", defaultValue = "true")
  private Boolean inclDeprecatedMethods;

  @Override
  public void executeMojo() {
    final Properties projectProperties = this.getProject().getProperties();
    final String oldValue = projectProperties.getProperty(SUREFIRE_ARG_LINE);
    final String newValue =
        createAgentConfigurations().prependVMArguments(oldValue, getAgentJarFile());
    getLog().info(SUREFIRE_ARG_LINE + " set to " + newValue);
    projectProperties.setProperty(SUREFIRE_ARG_LINE, newValue);
  }

  private File getAgentJarFile() {
    final Artifact gzoltarAgentArtifact = this.pluginArtifactMap.get(AGENT_ARTIFACT_NAME);
    return gzoltarAgentArtifact.getFile();
  }

  private AgentConfigs createAgentConfigurations() {
    final AgentConfigs agentConfigs = new AgentConfigs();

    String targetClassesDirectory = this.getProject().getBuild().getOutputDirectory();
    agentConfigs.setBuildLocation(targetClassesDirectory);
    agentConfigs.setDestfile(this.getDestFile().getAbsolutePath());

    if (this.getIncludes() != null && !this.getIncludes().isEmpty()) {
      final String agentIncludes = StringUtils.join(this.getIncludes().iterator(), ":");
      agentConfigs.setIncludes(agentIncludes);
    }

    if (this.getExcludes() != null && !this.getExcludes().isEmpty()) {
      final String agentExcludes = StringUtils.join(this.getExcludes().iterator(), ":");
      agentConfigs.setIncludes(agentExcludes);
    }

    if (this.exclClassLoaders != null) {
      agentConfigs.setExclClassloader(this.exclClassLoaders);
    }

    if (this.inclNoLocationClasses != null) {
      agentConfigs.setInclNoLocationClasses(this.inclNoLocationClasses.booleanValue());
    }

    if (this.output != null) {
      agentConfigs.setOutput(this.output);
    }

    if (this.granularity != null) {
      agentConfigs.setGranularity(this.granularity);
    }

    if (this.inclPublicMethods != null) {
      agentConfigs.setInclPublicMethods(this.inclPublicMethods.booleanValue());
    }

    if (this.inclStaticConstructors != null) {
      agentConfigs.setInclStaticConstructors(this.inclStaticConstructors.booleanValue());
    }

    if (this.inclDeprecatedMethods != null) {
      agentConfigs.setInclDeprecatedMethods(this.inclDeprecatedMethods.booleanValue());
    }

    return agentConfigs;
  }

  /**
   * @return the destFile
   */
  protected abstract File getDestFile();
}
