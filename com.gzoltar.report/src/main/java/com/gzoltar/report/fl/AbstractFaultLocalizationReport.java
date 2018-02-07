package com.gzoltar.report.fl;

import java.io.File;
import java.util.List;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.AbstractReport;
import com.gzoltar.report.metrics.IMetric;

public abstract class AbstractFaultLocalizationReport extends AbstractReport
    implements IFaultLocalizationReport {

  private final List<IFormula> formulas;

  /**
   * 
   * @param outputDirectory
   * @param formulas
   */
  protected AbstractFaultLocalizationReport(final File outputDirectory, final List<IMetric> metrics,
      final List<IFormula> formulas) {
    super(outputDirectory, metrics);
    this.formulas = formulas;
  }

  /**
   * {@inheritDoc}
   */
  public List<IFormula> getFormulas() {
    return this.formulas;
  }
}
