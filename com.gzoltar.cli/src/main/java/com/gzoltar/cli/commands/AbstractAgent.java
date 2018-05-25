package com.gzoltar.cli.commands;

import java.io.PrintStream;
import com.gzoltar.cli.Command;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.AgentOutput;

public abstract class AbstractAgent extends Command {

  /**
   * 
   * @return
   */
  protected AgentConfigs prepareAgentOptions() {
    AgentConfigs agentConfigs = new AgentConfigs();
    agentConfigs.setOutput(AgentOutput.NONE.name());
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
