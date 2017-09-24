package com.gzoltar.core;

public enum AgentOutput {

  /**
   * Value for the {@link AgentConfigs#OUTPUT} parameter: At VM termination execution data is
   * written to the file specified by {@link AgentConfigs#DESTFILE}.
   */
  FILE,

  /**
   * Value for the {@link AgentConfigs#OUTPUT} parameter: At VM termination execution data is
   * written to the console.
   */
  CONSOLE,

  /**
   * Value for the {@link AgentConfigs#OUTPUT} parameter: Do not produce any output.
   */
  NONE

}
