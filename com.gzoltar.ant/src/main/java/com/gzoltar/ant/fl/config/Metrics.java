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
