package com.gzoltar.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.MavenReportException;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.FaultLocalizationFamily;
import com.gzoltar.fl.FaultLocalization;
import com.gzoltar.report.ReportFormat;
import com.gzoltar.report.fl.FaultLocalizationReport;
import com.gzoltar.sfl.SFLFormulas;

@Mojo(name = "fl-report", defaultPhase = LifecyclePhase.VERIFY, threadSafe = true)
public class FaultLocalizationReportMojo extends AbstractReportMojo {

  /**
   * Output directory for the reports.
   */
  @Parameter(property = "gzoltar.outputDirectory",
      defaultValue = "${project.reporting.outputDirectory}/gzoltar")
  private File outputDirectory;

  /**
   * File with execution data.
   */
  @Parameter(property = "gzoltar.dataFile",
      defaultValue = "${project.build.directory}/gzoltar.exec")
  private File dataFile;

  @Parameter(property = "gzoltar.flFamily", defaultValue = "SFL")
  private String flFamily;

  @Parameter
  private List<String> formulas;

  @Override
  protected boolean canGenerateReportRegardingDataFiles() {
    return this.dataFile.exists();
  }

  @Override
  protected void executeReport(final Locale locale) throws MavenReportException {
    if (!this.isFLFamilyValid()) {
      getLog().info("Invalid fault localization family '" + this.flFamily.toString()
          + "'. Valid values are:");
      for (FaultLocalizationFamily family : FaultLocalizationFamily.values()) {
        getLog().info("  " + family.name());
      }
      return;
    }

    if (!this.areFormulasValid()) {
      getLog().info("Invalid formulas type '" + this.formulas.toString() + "'. Valid values are:");
      for (SFLFormulas formula : SFLFormulas.values()) {
        getLog().info("  " + formula.name());
      }
      return;
    }

    try {
      // first diagnose it
      FaultLocalization fl = new FaultLocalization(
          FaultLocalizationFamily.valueOf(this.flFamily.toUpperCase(Locale.ENGLISH)), this.formulas);
      ISpectrum spectrum = fl.diagnose(this.dataFile);

      // then report it
      FaultLocalizationReport report = new FaultLocalizationReport(
          ReportFormat.valueOf(this.format.toUpperCase(Locale.ENGLISH)), this.outputDirectory);
      report.generateReport(spectrum);
    } catch (IOException e) {
      getLog().info(e);
      e.printStackTrace();
    }
  }

  @Override
  protected String getOutputDirectory() {
    return this.outputDirectory.getAbsolutePath();
  }

  public String getFLFamily() {
    return this.flFamily;
  }

  public boolean isFLFamilyValid() {
    try {
      return FaultLocalizationFamily.valueOf(this.flFamily.toUpperCase(Locale.ENGLISH)) != null;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public List<String> getFormulas() {
    return this.formulas;
  }

  public boolean areFormulasValid() {
    for (String formula : this.formulas) {
      try {
        SFLFormulas.valueOf(formula.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException e) {
        return false;
      }
    }
    return true;
  }
}
