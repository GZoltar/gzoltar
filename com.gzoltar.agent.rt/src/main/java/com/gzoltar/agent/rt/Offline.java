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
