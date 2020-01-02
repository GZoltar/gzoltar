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
