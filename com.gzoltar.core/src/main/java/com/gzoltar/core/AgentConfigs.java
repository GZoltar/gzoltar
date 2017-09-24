package com.gzoltar.core;

import static java.lang.String.format;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.instr.granularity.GranularityFactory.GranularityLevel;
import com.gzoltar.core.model.NodeType;
import com.gzoltar.core.util.CommandLineSupport;

/**
 * Utility to create and parse configurations for the runtime agent. Configurations are represented
 * as a string in the following format:
 * 
 * <pre>
 *   key1=value1,key2=value2,key3=value3
 * </pre>
 */
public final class AgentConfigs {

  /**
   * 
   */
  public static final String BUILDLOCATION_KEY = "buildlocation";

  /**
   * 
   */
  public static final String DEFAULT_BUILDLOCATION = "";

  /**
   * Specifies the output file for execution data. Default is <code>gzoltar.exec</code> in the
   * working directory.
   */
  public static final String DESTFILE_KEY = "destfile";

  /**
   * Default value for the "destfile" agent configuration.
   */
  public static final String DEFAULT_DESTFILE = "gzoltar.exec";

  /**
   * Wildcard expression for class names that should be included for code coverage. Default is
   * <code>*</code> (all classes included).
   * 
   * @see WildcardMatcher
   */
  public static final String INCLUDES_KEY = "includes";

  public static final String DEFAULT_INCLUDES = "*";

  /**
   * Wildcard expression for class names that should be excluded from code coverage. Default is the
   * empty string (no exclusions).
   * 
   * @see WildcardMatcher
   */
  public static final String EXCLUDES_KEY = "excludes";

  public static final String DEFAULT_EXCLUDES = "";

  /**
   * Wildcard expression for class loaders names for classes that should be excluded from code
   * coverage. This means all classes loaded by a class loader which full qualified name matches
   * this expression will be ignored for code coverage regardless of all other filtering settings.
   * Default is <code>sun.reflect.DelegatingClassLoader</code>.
   * 
   * @see WildcardMatcher
   */
  public static final String EXCLCLASSLOADER_KEY = "exclclassloader";

  public static final String DEFAULT_EXCLCLASSLOADER = "sun.reflect.DelegatingClassLoader";

  /**
   * Specifies whether also classes without a source location should be instrumented. Normally such
   * classes are generated at runtime e.g. by mocking frameworks and are therefore excluded by
   * default. Default is <code>false</code>.
   */
  public static final String INCLNOLOCATIONCLASSES_KEY = "inclnolocationclasses";

  public static final boolean DEFAULT_INCLNOLOCATIONCLASSES = false;

  /**
   * Specifies the output mode. Default is {@link OutputMode#console}.
   * 
   * @see OutputMode#file
   * @see OutputMode#console
   * @see OutputMode#none
   */
  public static final String OUTPUT_KEY = "output";

  public static final OutputMode DEFAULT_OUTPUT = OutputMode.console;

  /**
   * Specifies the granularity level of instrumentation. Default is {@link GranularityLevel#line}.
   */
  public static final String GRANULARITY_KEY = "granularity";

  public static final GranularityLevel DEFAULT_GRANULARITY = GranularityLevel.line;

  /**
   * Specifies whether public methods of each class under test should be instrumented. Default is
   * <code>true</code>.
   */
  public static final String INCLPUBLICMETHODS_KEY = "inclpublicmethods";

  public static final boolean DEFAULT_INCLPUBLICMETHODS = true;

  private final Map<String, String> configs;

  private static final Collection<String> VALID_CONFIGS = Arrays.asList(BUILDLOCATION_KEY,
      DESTFILE_KEY, INCLUDES_KEY, EXCLUDES_KEY, EXCLCLASSLOADER_KEY, INCLNOLOCATIONCLASSES_KEY,
      OUTPUT_KEY, GRANULARITY_KEY, INCLPUBLICMETHODS_KEY);

  private static final Pattern CONFIG_SPLIT = Pattern.compile(",(?=[a-zA-Z0-9_\\-]+=)");

  /**
   * New instance with all values set to default.
   */
  public AgentConfigs() {
    this.configs = new HashMap<String, String>();
  }

  /**
   * New instance parsed from the given configuration string.
   * 
   * @param agentConfigs string to parse or <code>null</code>
   */
  public AgentConfigs(final String agentConfigs) {
    this();
    if (agentConfigs != null && agentConfigs.length() > 0) {
      for (final String entry : CONFIG_SPLIT.split(agentConfigs)) {
        final int pos = entry.indexOf('=');
        if (pos == -1) {
          throw new IllegalArgumentException(
              format("Invalid agent configuration syntax \"%s\".", agentConfigs));
        }
        final String key = entry.substring(0, pos);
        if (!VALID_CONFIGS.contains(key)) {
          throw new IllegalArgumentException(format("Unknown agent configuration \"%s\".", key));
        }

        final String value = entry.substring(pos + 1);
        this.setConfig(key, value);
      }
    }
  }

  private void setConfig(final String key, final String value) {
    this.configs.put(key, value);
  }

  private String getConfig(final String key, final String defaultValue) {
    final String value = this.configs.get(key);
    return value == null ? defaultValue : value;
  }

  private void setConfig(final String key, final boolean value) {
    this.setConfig(key, Boolean.toString(value));
  }

  private boolean getConfig(final String key, final boolean defaultValue) {
    final String value = this.configs.get(key);
    return value == null ? defaultValue : Boolean.parseBoolean(value);
  }

  /**
   * Returns the build location.
   * 
   * @return build directory location
   */
  public String getBuildLocation() {
    return this.getConfig(BUILDLOCATION_KEY, DEFAULT_BUILDLOCATION);
  }

  /**
   * Set the build location.
   * 
   * @param buildLocation directory location
   */
  public void setBuildLocation(final String buildLocation) {
    this.setConfig(BUILDLOCATION_KEY, buildLocation);
  }

  /**
   * Returns the output file location.
   * 
   * @return output file location
   */
  public String getDestfile() {
    return this.getConfig(DESTFILE_KEY, DEFAULT_DESTFILE);
  }

  /**
   * Sets the output file location.
   * 
   * @param destfile output file location
   */
  public void setDestfile(final String destfile) {
    this.setConfig(DESTFILE_KEY, destfile);
  }

  /**
   * Returns the wildcard expression for classes to include.
   * 
   * @return wildcard expression for classes to include
   * @see WildcardMatcher
   */
  public String getIncludes() {
    return this.getConfig(INCLUDES_KEY, DEFAULT_INCLUDES);
  }

  /**
   * Sets the wildcard expression for classes to include.
   * 
   * @param includes wildcard expression for classes to include
   * @see WildcardMatcher
   */
  public void setIncludes(final String includes) {
    this.setConfig(INCLUDES_KEY, includes);
  }

  /**
   * Returns the wildcard expression for classes to exclude.
   * 
   * @return wildcard expression for classes to exclude
   * @see WildcardMatcher
   */
  public String getExcludes() {
    return this.getConfig(EXCLUDES_KEY, DEFAULT_EXCLUDES);
  }

  /**
   * Sets the wildcard expression for classes to exclude.
   * 
   * @param excludes wildcard expression for classes to exclude
   * @see WildcardMatcher
   */
  public void setExcludes(final String excludes) {
    this.setConfig(EXCLUDES_KEY, excludes);
  }

  /**
   * Returns the wildcard expression for excluded class loaders.
   * 
   * @return expression for excluded class loaders
   * @see WildcardMatcher
   */
  public String getExclClassloader() {
    return this.getConfig(EXCLCLASSLOADER_KEY, DEFAULT_EXCLCLASSLOADER);
  }

  /**
   * Sets the wildcard expression for excluded class loaders.
   * 
   * @param expression expression for excluded class loaders
   * @see WildcardMatcher
   */
  public void setExclClassloader(final String expression) {
    this.setConfig(EXCLCLASSLOADER_KEY, expression);
  }

  /**
   * Returns whether classes without source location should be instrumented.
   * 
   * @return <code>true</code> if classes without source location should be instrumented
   */
  public boolean getInclNoLocationClasses() {
    return this.getConfig(INCLNOLOCATIONCLASSES_KEY, DEFAULT_INCLNOLOCATIONCLASSES);
  }

  /**
   * Sets whether classes without source location should be instrumented.
   * 
   * @param include <code>true</code> if classes without source location should be instrumented
   */
  public void setInclNoLocationClasses(final boolean include) {
    this.setConfig(INCLNOLOCATIONCLASSES_KEY, include);
  }

  /**
   * Returns the output mode
   * 
   * @return current output mode
   */
  public OutputMode getOutput() {
    final String value = this.configs.get(OUTPUT_KEY);
    return value == null ? DEFAULT_OUTPUT : OutputMode.valueOf(value);
  }

  /**
   * Sets the output mode
   * 
   * @param output Output mode
   */
  public void setOutput(final String output) {
    setOutput(OutputMode.valueOf(output));
  }

  /**
   * Sets the output mode
   * 
   * @param output Output mode
   */
  public void setOutput(final OutputMode output) {
    this.setConfig(OUTPUT_KEY, output.name());
  }

  /**
   * Returns the granularity level
   * 
   * @return current granularity level
   */
  public GranularityLevel getGranularity() {
    final String value = this.configs.get(GRANULARITY_KEY);
    return value == null ? DEFAULT_GRANULARITY : GranularityLevel.valueOf(value);
  }

  /**
   * Sets the granularity level
   * 
   * @param granularity Granularity level
   */
  public void setGranularity(final String granularity) {
    setGranularity(GranularityLevel.valueOf(granularity));
  }

  /**
   * Sets the granularity level
   * 
   * @param granularity Granularity level
   */
  public void setGranularity(final GranularityLevel granularity) {
    this.setConfig(GRANULARITY_KEY, granularity.name());
  }

  /**
   * Returns whether public methods should be instrumented.
   * 
   * @return <code>true</code> if public methods should be instrumented
   */
  public Boolean getInclPublicMethods() {
    return this.getConfig(INCLPUBLICMETHODS_KEY, DEFAULT_INCLPUBLICMETHODS);
  }

  /**
   * Sets whether public methods should be instrumented.
   * 
   * @param inclPublicMethods <code>true</code> if public methods should be instrumented
   */
  public void setInclPublicMethods(final boolean inclPublicMethods) {
    this.setConfig(INCLPUBLICMETHODS_KEY, inclPublicMethods);
  }

  public IEventListener getEventListener() {
    return new IEventListener() {
      @Override
      public void endTransaction(String transactionName, boolean[] activity, int hashCode,
          boolean isError) {}

      @Override
      public void endTransaction(String transactionName, boolean[] activity, boolean isError) {}

      @Override
      public void endSession() {}

      @Override
      public void addProbe(int id, int nodeId) {}

      @Override
      public void addNode(int id, String name, NodeType type, int parentId) {}
    };
  }

  /**
   * Generate required JVM argument based on current configuration and supplied agent jar location.
   * 
   * @param agentJarFile location of the GZoltar Agent Jar
   * @return Argument to pass to create new VM with coverage enabled
   */
  public String getVMArgument(final File agentJarFile) {
    return format("-javaagent:%s=%s", agentJarFile, this);
  }

  /**
   * Generate required quoted JVM argument based on current configuration and supplied agent jar
   * location.
   * 
   * @param agentJarFile location of the GZoltar Agent Jar
   * @return Quoted argument to pass to create new VM with coverage enabled
   */
  public String getQuotedVMArgument(final File agentJarFile) {
    return CommandLineSupport.quote(getVMArgument(agentJarFile));
  }

  /**
   * Generate required quotes JVM argument based on current configuration and prepends it to the
   * given argument command line. If a agent with the same JAR file is already specified this
   * parameter is removed from the existing command line.
   * 
   * @param arguments existing command line arguments or <code>null</code>
   * @param agentJarFile location of the GZoltar Agent Jar
   * @return VM command line arguments prepended with configured GZoltar agent
   */
  public String prependVMArguments(final String arguments, final File agentJarFile) {
    final List<String> args = CommandLineSupport.split(arguments);
    final String plainAgent = format("-javaagent:%s", agentJarFile);
    for (final Iterator<String> i = args.iterator(); i.hasNext();) {
      if (i.next().startsWith(plainAgent)) {
        i.remove();
      }
    }
    args.add(0, getVMArgument(agentJarFile));
    return CommandLineSupport.quote(args);
  }

  /**
   * Creates a string representation that can be passed to the agent via the command line. Might be
   * the empty string, if no configurations are set.
   */
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    for (final String key : VALID_CONFIGS) {
      final String value = this.configs.get(key);
      if (value != null) {
        if (sb.length() > 0) {
          sb.append(',');
        }
        sb.append(key).append('=').append(value);
      }
    }
    return sb.toString();
  }

  /**
   * Possible values for {@link AgentConfigs#OUTPUT}.
   */
  public static enum OutputMode {
    /**
     * Value for the {@link AgentConfigs#OUTPUT} parameter: At VM termination execution data is
     * written to the file specified by {@link AgentConfigs#DESTFILE}.
     */
    file,

    /**
     * Value for the {@link AgentConfigs#OUTPUT} parameter: At VM termination execution data is
     * written to the console.
     */
    console,

    /**
     * Value for the {@link AgentConfigs#OUTPUT} parameter: Do not produce any output.
     */
    none
  }
}
