package com.gzoltar.ant.fl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import com.gzoltar.ant.AbstractReportTask;
import com.gzoltar.ant.fl.config.FaultLocalizationFamilies;
import com.gzoltar.ant.fl.config.FaultLocalizationFamilies.FaultLocalizationFamilyElement;
import com.gzoltar.report.fl.FaultLocalizationReportBuilder;
import com.gzoltar.report.fl.config.ConfigFaultLocalizationFamily;

/**
 * Fault localization report
 */
public class FaultLocalizationReportTask extends AbstractReportTask {

  private final FaultLocalizationFamilies flFamilies = new FaultLocalizationFamilies();

  /**
   * 
   */
  public FaultLocalizationFamilies createFlFamilies() {
    return this.flFamilies;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void generateReport(Locale locale) {

    List<ConfigFaultLocalizationFamily> configFlFamilies =
        new ArrayList<ConfigFaultLocalizationFamily>();
    for (FaultLocalizationFamilyElement flFamily : this.flFamilies.getFlFamilies()) {
      ConfigFaultLocalizationFamily configFlFamily = new ConfigFaultLocalizationFamily();

      // set fault localization family
      configFlFamily.setFaultLocalizationFamily(flFamily.getFaultLocalizationFamily());
      // set formulas
      configFlFamily.setFormulas(flFamily.getFormulasGroup().getNameOfFormulas());
      // set metrics
      configFlFamily.setMetrics(flFamily.getMetricsGroup().getNameOfMetrics());
      // set formatters
      configFlFamily.setFormatters(flFamily.getFormattersGroup().getFormatters());

      configFlFamilies.add(configFlFamily);
    }

    try {
      // build a fault localization report
      FaultLocalizationReportBuilder.build(this.outputDirectory, this.dataFile, configFlFamilies);
    } catch (Exception e) {
      throw new BuildException(e);
    }
  }
}
