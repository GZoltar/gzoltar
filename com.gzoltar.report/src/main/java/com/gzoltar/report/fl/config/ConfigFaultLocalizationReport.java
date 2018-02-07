package com.gzoltar.report.fl.config;

import java.util.ArrayList;
import java.util.List;
import com.gzoltar.fl.FaultLocalizationFamily;
import com.gzoltar.report.IReportFormat;
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
            <formats>
              <format>
                <name>TXT</name>
              </format>
              <format>
                <name>HTML</name>
                <htmlViews>
                  <htmlView>SUNburst</htmlView>
                  <htmlView>VERTICAL_PARTITION</htmlView>
                  <htmlView>...</htmlView>
                </htmlViews>
              </format>
            </formats>
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
    sfl.setFormats(getDefaultFormats());

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

  private static List<IReportFormat> getDefaultFormats() {
    List<IReportFormat> formats = new ArrayList<IReportFormat>();
    formats.add(new ConfigTxtReportFormat());
    formats.add(new ConfigHTMLReportFormat());

    return formats;
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
