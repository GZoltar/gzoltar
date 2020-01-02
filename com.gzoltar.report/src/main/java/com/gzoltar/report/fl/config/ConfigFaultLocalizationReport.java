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
import com.gzoltar.fl.FaultLocalizationFamily;
import com.gzoltar.report.IReportFormatter;
import com.gzoltar.report.metrics.Metric;
import com.gzoltar.sfl.SFLFormulas;

public class ConfigFaultLocalizationReport {

  /**
   * <code>
      <configuration>
        <flFamilies>
          <flFamily>
            <name>SFL</name>
            <formulas>
              <formula>OchIAI</formula>
              <formula>NAISH1</formula>
              <formula>KulCZYNski2</formula>
              <formula>...</formula>
            </formulas>
            <metrics>
              <metric>RHO</metric>
              <metric>ambiGUITY</metric>
              <metric>EntROPY</metric>
              <metric>...</metric>
            </metrics>
            <formatters>
              <formatter>
                <name>TXT</name>
              </formatter>
              <formatter>
                <name>HTML</name>
                <htmlViews>
                  <htmlView>SUNburst</htmlView>
                  <htmlView>VERTICAL_PARTITION</htmlView>
                  <htmlView>...</htmlView>
                </htmlViews>
              </formatter>
            </formatters>
          </flFamily>
          <flFamily>
            ...
          </flFamily>
        </flFamilies>
      </configuration>
     </code>
   */
  public static List<ConfigFaultLocalizationFamily> getDefaults() {

    /**
     * SFL family
     */
    ConfigFaultLocalizationFamily sfl = new ConfigFaultLocalizationFamily();
    sfl.setName(FaultLocalizationFamily.SFL.name());
    sfl.setFormulas(getDefaultSFLFormulas());
    sfl.setMetrics(getDefaultMetrics());
    sfl.setFormatters(getDefaultFormatters());

    /**
     * Families
     */

    List<ConfigFaultLocalizationFamily> flFamilies = new ArrayList<ConfigFaultLocalizationFamily>();
    flFamilies.add(sfl);

    return flFamilies;
  }

  private static List<String> getDefaultSFLFormulas() {
    List<String> sflFormulas = new ArrayList<String>();
    sflFormulas.add(SFLFormulas.BARINEL.name());
    sflFormulas.add(SFLFormulas.DSTAR.name());
    sflFormulas.add(SFLFormulas.OCHIAI.name());
    sflFormulas.add(SFLFormulas.TARANTULA.name());
    return sflFormulas;
  }

  private static List<String> getDefaultMetrics() {
    List<String> metrics = new ArrayList<String>();
    metrics.add(Metric.RHO.name());
    metrics.add(Metric.AMBIGUITY.name());
    metrics.add(Metric.ENTROPY.name());
    return metrics;
  }

  private static List<IReportFormatter> getDefaultFormatters() {
    List<IReportFormatter> formatters = new ArrayList<IReportFormatter>();
    formatters.add(new ConfigTxtReportFormatter());
    formatters.add(new ConfigHTMLReportFormatter());

    return formatters;
  }

  /**
   * 
   * @param flFamilies
   */
  public static void setDefaultsIfNotPresent(List<ConfigFaultLocalizationFamily> flFamilies) {
    for (ConfigFaultLocalizationFamily flFamily : flFamilies) {
      // SFL family
      if (FaultLocalizationFamily.SFL.name().equals(flFamily.getName())) {
        if (!flFamily.hasFormulas()) {
          flFamily.setFormulas(getDefaultSFLFormulas());
        }
      }
      // add here other families
    }
  }
}
