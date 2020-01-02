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
package com.gzoltar.ant.fl.config;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.types.resources.Union;

public class Metrics extends Union {

  private final List<MetricElement> metrics = new ArrayList<MetricElement>();

  /**
   * 
   * @return
   */
  public MetricElement createMetric() {
    final MetricElement metric = new MetricElement();
    this.metrics.add(metric);
    return metric;
  }

  /**
   * 
   * @return
   */
  public List<MetricElement> getMetrics() {
    return this.metrics;
  }

  /**
   * 
   * @return
   */
  public List<String> getNameOfMetrics() {
    List<String> metricsNames = new ArrayList<String>();
    for (MetricElement metric : this.metrics) {
      metricsNames.add(metric.getName());
    }
    return metricsNames;
  }

  /**
   * 
   */
  public class MetricElement {

    private String name;

    /**
     * 
     * @param name
     */
    public void setName(final String name) {
      this.name = name;
    }

    /**
     * 
     * @return
     */
    public String getName() {
      return this.name;
    }
  }
}
