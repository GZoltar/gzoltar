package com.gzoltar.report;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;

public abstract class AbstractReport implements IReport {

  protected final File outputDirectory;

  protected final List<IFormula> formulas;

  protected AbstractReport(File outputDirectory, final List<IFormula> formulas) {
    this.outputDirectory = outputDirectory;
    this.formulas = formulas;
  }

  /**
   * {@inheritDoc}
   */
  public File getOutputDirectory() {
    return this.outputDirectory;
  }

  /**
   * {@inheritDoc}
   */
  public List<IFormula> getFormulas() {
    return this.formulas;
  }

  /**
   * {@inheritDoc}
   */
  public abstract void generateReport(final ISpectrum spectrum) throws IOException;
}
