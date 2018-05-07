package com.gzoltar.cli.commands;

import java.io.PrintStream;
import org.kohsuke.args4j.Option;
import com.gzoltar.cli.Command;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.AgentOutput;

public abstract class AbstractAgent extends Command {

  /**
   * Specifies whether public methods of each class under test should be instrumented. Default is
   * <code>true</code>.
   */
  @Option(name = "--inclPublicMethods", required = false)
  private Boolean inclPublicMethods = true;

  /**
   * Specifies whether public static constructors of each class under test should be instrumented.
   * Default is <code>false</code>.
   */
  @Option(name = "--inclStaticConstructors", required = false)
  private Boolean inclStaticConstructors = false;

  /**
   * Specifies whether methods annotated with @deprecated of each class under test should be
   * instrumented. Default is <code>true</code>.
   */
  @Option(name = "--inclDeprecatedMethods", required = false)
  private Boolean inclDeprecatedMethods = true;

  /**
   * Specifies the granularity level. Valid options are:
   * <ul>
   * <li>line (default)</li>
   * <li>method</li>
   * <li>basicblock</li>
   * </ul>
   */
  @Option(name = "--granularity", metaVar = "<level>", required = false)
  private String granularity = "LINE";

  /**
   * 
   * @return
   */
  protected AgentConfigs prepareAgentOptions() {
    AgentConfigs agentConfigs = new AgentConfigs();

    agentConfigs.setOutput(AgentOutput.NONE.name());
    agentConfigs.setGranularity(this.granularity);
    agentConfigs.setInclPublicMethods(this.inclPublicMethods.booleanValue());
    agentConfigs.setInclStaticConstructors(this.inclStaticConstructors.booleanValue());
    agentConfigs.setInclDeprecatedMethods(this.inclDeprecatedMethods.booleanValue());

    return agentConfigs;
  }

  /**
   * {@inheritDoc}
   */
  public abstract String description();

  /**
   * {@inheritDoc}
   */
  public abstract int execute(PrintStream out, PrintStream err) throws Exception;
}
