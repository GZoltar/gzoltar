package com.gzoltar.report.fl.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.gzoltar.fl.FaultLocalizationFamily;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.IReportFormat;
import com.gzoltar.report.metrics.IMetric;
import com.gzoltar.report.metrics.Metric;
import com.gzoltar.sfl.SFLFormulas;

public class ConfigFaultLocalizationFamily {

  private FaultLocalizationFamily faultLocalizationFamily;

  private List<IFormula> formulas;

  private List<IMetric> metrics;

  private List<IReportFormat> formats;

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

  public void setFormats(List<IReportFormat> formats) {
    this.formats = formats;
  }

  public List<IReportFormat> getFormats() {
    return this.formats;
  }

  public boolean hasReportFormats() {
    return !this.formats.isEmpty();
  }
}
