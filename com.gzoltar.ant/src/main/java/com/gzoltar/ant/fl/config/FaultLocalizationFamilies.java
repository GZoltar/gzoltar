package com.gzoltar.ant.fl.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.tools.ant.types.resources.Union;
import com.gzoltar.fl.FaultLocalizationFamily;

public class FaultLocalizationFamilies extends Union {

  private final List<FaultLocalizationFamilyElement> flFamilies =
      new ArrayList<FaultLocalizationFamilyElement>();

  /**
   * 
   * @return
   */
  public FaultLocalizationFamilyElement createFlFamily() {
    FaultLocalizationFamilyElement flFamily = new FaultLocalizationFamilyElement();
    this.flFamilies.add(flFamily);
    return flFamily;
  }

  /**
   * 
   * @return
   */
  public List<FaultLocalizationFamilyElement> getFlFamilies() {
    return this.flFamilies;
  }

  /**
   * 
   */
  public class FaultLocalizationFamilyElement extends Union {

    private String faultLocalizationFamilyName;

    private FaultLocalizationFamily faultLocalizationFamily;

    private final Formulas formulas = new Formulas();

    private final Metrics metrics = new Metrics();

    private final Formatters formatters = new Formatters();

    /**
     * 
     * @param faultLocalizationFamilyName
     */
    public void setName(final String faultLocalizationFamilyName) {
      this.faultLocalizationFamilyName = faultLocalizationFamilyName;
      this.faultLocalizationFamily =
          FaultLocalizationFamily.valueOf(faultLocalizationFamilyName.toUpperCase(Locale.ENGLISH));
    }

    /**
     * 
     * @return
     */
    public String getName() {
      return this.faultLocalizationFamilyName;
    }

    /**
     * 
     * @return
     */
    public FaultLocalizationFamily getFaultLocalizationFamily() {
      if (this.faultLocalizationFamily == null) {
        throw new RuntimeException("<flFamily> tag requires a 'name' value");
      }
      return this.faultLocalizationFamily;
    }

    /**
     * 
     * @return
     */
    public Formulas createFormulas() {
      return this.formulas;
    }

    /**
     * 
     * @return
     */
    public Formulas getFormulasGroup() {
      return this.formulas;
    }

    /**
     * 
     * @return
     */
    public Metrics createMetrics() {
      return this.metrics;
    }

    /**
     * 
     * @return
     */
    public Metrics getMetricsGroup() {
      return this.metrics;
    }

    /**
     * 
     * @return
     */
    public Formatters createFormatters() {
      return this.formatters;
    }

    /**
     * 
     * @return
     */
    public Formatters getFormattersGroup() {
      return this.formatters;
    }
  }
}
