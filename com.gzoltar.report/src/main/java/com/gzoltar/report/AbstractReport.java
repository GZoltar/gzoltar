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
package com.gzoltar.report;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.report.metrics.IMetric;

public abstract class AbstractReport implements IReport {

  private final File outputDirectory;

  private final List<IMetric> metrics;

  protected AbstractReport(final File outputDirectory, final List<IMetric> metrics) {
    this.outputDirectory = outputDirectory;
    this.metrics = metrics;
  }

  /**
   * {@inheritDoc}
   */
  public File getOutputDirectory() {
    return this.outputDirectory;
  }

  public List<IMetric> getMetrics() {
    return this.metrics;
  }

  /**
   * {@inheritDoc}
   */
  public abstract void generateReport(final ISpectrum spectrum) throws IOException;
}
