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

  private final FaultLocalizationFamilies flFamilies;

  public FaultLocalizationReportTask() {
    super();
    this.flFamilies = new FaultLocalizationFamilies();
  }

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
      FaultLocalizationReportBuilder.build(this.buildLocation.getAbsolutePath(),
          this.agentConfigs, this.outputDirectory, this.dataFile, configFlFamilies);
    } catch (Exception e) {
      throw new BuildException(e);
    }
  }
}
