package com.gzoltar.agent.rt;

import java.util.Properties;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.runtime.Collector;

/**
 * The API for classes instrumented in "offline" mode. The agent configuration is provided through
 * system properties prefixed with <code>gzoltar-agent.</code>.
 */
public final class Offline {

  static {
    final Properties config = ConfigLoader.load(System.getProperties());
    IAgent agent = Agent.getInstance(new AgentConfigs(config));
    agent.startup();
  }

  private Offline() {
    // no instance
  }

  /**
   * API for offline instrumented classes.
   * 
   * @param args
   */
  public synchronized static void getHitArray(final Object[] args) {
    Collector.instance().getHitArray(args);
  }

}
