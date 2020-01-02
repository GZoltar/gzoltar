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
package com.gzoltar.report.fl.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.gzoltar.fl.FaultLocalizationFamily;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.IReportFormatter;
import com.gzoltar.report.metrics.IMetric;
import com.gzoltar.report.metrics.Metric;
import com.gzoltar.sfl.SFLFormulas;

public class ConfigFaultLocalizationFamily {

  private FaultLocalizationFamily faultLocalizationFamily;

  private List<IFormula> formulas;

  private List<IMetric> metrics;

  private List<IReportFormatter> formatters;

  public void setName(String faultLocalizationFamilyName) {
    this.faultLocalizationFamily =
        FaultLocalizationFamily.valueOf(faultLocalizationFamilyName.toUpperCase(Locale.ENGLISH));
  }

  public String getName() {
    return this.faultLocalizationFamily.name();
  }

  public FaultLocalizationFamily getFaultLocalizationFamily() {
    return this.faultLocalizationFamily;
  }

  public void setFaultLocalizationFamily(FaultLocalizationFamily faultLocalizationFamily) {
    this.faultLocalizationFamily = faultLocalizationFamily;
  }

  public void setFormulas(List<String> formulas) {
    this.formulas = new ArrayList<IFormula>();
    for (String formula : formulas) {
      switch (this.faultLocalizationFamily) {
        case SFL:
          this.formulas.add(SFLFormulas.valueOf(formula.toUpperCase(Locale.ENGLISH)).getFormula());
          break;
      }
    }
  }

  public List<IFormula> getFormulas() {
    return this.formulas;
  }

  public boolean hasFormulas() {
    return !this.formulas.isEmpty();
  }

  public void setMetrics(List<String> metrics) {
    this.metrics = new ArrayList<IMetric>();
    for (String metric : metrics) {
      this.metrics.add(Metric.valueOf(metric.toUpperCase(Locale.ENGLISH)).getMetric());
    }
  }

  public List<IMetric> getMetrics() {
    return this.metrics;
  }

  public boolean hasMetrics() {
    return !this.metrics.isEmpty();
  }

  public void setFormatters(List<IReportFormatter> formatters) {
    this.formatters = formatters;
  }

  public List<IReportFormatter> getFormatters() {
    return this.formatters;
  }

  public boolean hasReportFormatters() {
    return !this.formatters.isEmpty();
  }
}
